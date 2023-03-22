package me.marcolvr.server.game.player;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.packet.serverbound.ServerboundFicheAction;
import me.marcolvr.server.Main;
import me.marcolvr.server.game.BlackJackRoom;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.serverbound.ServerboundRoom;
import me.marcolvr.server.game.logic.card.BlackJackCard;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BlackJackPlayer {

    private final PlayerConnection connection;
    private final String username;
    private BlackJackRoom room;
    private int fiches;
    private int lastTransaction;
    private boolean lastTransactionFromClient;
    private int allowClientTransactions;
    private List<BlackJackCard> cards;

    public BlackJackPlayer(PlayerConnection connection, String username){
        this.connection = connection;
        this.username=username;
        fiches=10000;
        lastTransactionFromClient=false;

        cards=new ArrayList<>();
        connection.onConnectionError((e)->{
            Main.getBlackJackServer().disconnect(this, "Connection Error: " + e.getMessage());
        });
        connection.onPacketReceive((byte) 3, (packet)->{
            if(room!=null || !Main.getBlackJackServer().joinRoom(this, ((ServerboundRoom) packet).getRoom())){
                connection.sendPacket(new ClientboundNACK((byte) 0x03));
            }else{
                connection.sendPacket(new ClientboundACK((byte) 0x03));
                connection.sendPacket(new ClientboundLobbyUpdate(room.getState()==1, room.getTime(), room.getPlayers().size()));
            }
        });
        connection.onPacketReceive((byte) 5, (packet)->{
            ServerboundFicheAction action = (ServerboundFicheAction) packet;
            if(allowClientTransactions==0 || action.getFiches()==0) {
                connection.sendPacket(new ClientboundNACK((byte) 0x05));
                return;
            }
            if(action.getFiches()>0){
                if(allowClientTransactions!=1){
                    connection.sendPacket(new ClientboundNACK((byte) 0x05));
                    return;
                }
            }
            if(action.getFiches()<0){
                if(allowClientTransactions!=-1 || fiches+action.getFiches()<0){
                    connection.sendPacket(new ClientboundNACK((byte) 0x05));
                    return;
                }
            }
            fiches+=action.getFiches();
            lastTransactionFromClient=true;
            lastTransaction=action.getFiches();
            allowClientTransactions=0;
            connection.sendPacket(new ClientboundACK((byte) 0x05));
        });

    }

    public void allowTransactions(boolean add){
        allowClientTransactions=add ? 1 : -1;
    }

    public int makeFichesTransaction(int qt, boolean add, boolean fromClient){
        qt=Math.abs(qt);
        if(add){
            fiches+=qt;
            lastTransaction=qt;
        }else{
            if(fiches-qt<0) return -1;
            fiches-=qt;
            lastTransaction=qt*-1;
        }
        lastTransactionFromClient=fromClient;
        return fiches;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof BlackJackPlayer p)) return false;
        return username == null ? connection.getAddress().equals(p.connection.getAddress()) : username.equalsIgnoreCase(p.username);
    }
}

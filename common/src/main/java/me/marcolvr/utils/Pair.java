package me.marcolvr.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Pair<F, S> {
    F first;
    S second;
}

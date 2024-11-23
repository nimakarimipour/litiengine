package de.gurkenlabs.litiengine;

import javax.annotation.Nullable;

public class Nullability {

    public static <T> T castToNonnull(@Nullable T param){
        if(param == null){
            throw new RuntimeException("Param is null");
        }
        return param;
    }
}

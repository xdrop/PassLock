package me.xdrop.passlock.model;

import java.util.List;

public interface BufferedProcessor<T> {

    int getBufferSize();
    
    void receive(List<? extends T> in);
    
    void process() throws Exception;

    List<T> send();
    
}

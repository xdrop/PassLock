package me.xdrop.passlock.model;

import java.util.List;

public interface BufferedProcessor<T> {

    void setBufferSize(int size);

    int getBufferSize();
    
    void receive(List<? extends T> in);
    
    List<T> process() throws Exception;
    
    void send(List<? extends T> out);
    
}

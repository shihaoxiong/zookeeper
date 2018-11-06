package com.java.zookeeper.curator.scene.eventListener.config;

import java.io.*;

/**
 * 〈序列化工具 〉
 *
 * @author xsh
 * @date 2018/10/27
 * @since 1.0.0
 */
public class SerializeUtil {


    public static byte[]  serialize(Object  source ) {
        byte[] target = null;
        try{
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream);
            outputStream.writeObject(source);
            target = byteOutputStream.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return target;
    }


    public static Environment  deserialize(byte[]   source ) {
        Environment  target = null;
        try{
            ByteArrayInputStream byteOutputStream = new ByteArrayInputStream(source);
            ObjectInputStream outputStream = new ObjectInputStream(byteOutputStream);
            target  = (Environment)outputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return target;
    }

}
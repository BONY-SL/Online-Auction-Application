package com.example.BidZone.util;

import com.example.BidZone.dto.MessageDTO;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class RealTimeMessageManage {

    synchronized public void serializeMesageDTOInventory(MessageDTO messageDTO) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("message.ser"))) {
            oos.writeObject(messageDTO);
            System.out.println("User MessageDTO serialized successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }
    }

    synchronized public MessageDTO deserializeMesageDTOInventory(String filename) {

        MessageDTO messageDTO = new MessageDTO();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {

            messageDTO = (MessageDTO) ois.readObject();


            System.out.println("User MessageDTO deserialized successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred during deserialization: " + e.getMessage());
        }
        return messageDTO;
    }
}

package com.example.BidZone.util;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class OTPMange {

    synchronized public void serializeInventory(String filename, UserMailAndOTPSerailzeble userMailAndOTPSerailzeble) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(userMailAndOTPSerailzeble);
            System.out.println("User OTP serialized successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }
    }

    synchronized public UserMailAndOTPSerailzeble deserializeInventory(String filename) {

        UserMailAndOTPSerailzeble userMailAndOTPSerailzeble = new UserMailAndOTPSerailzeble();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {

            userMailAndOTPSerailzeble = (UserMailAndOTPSerailzeble) ois.readObject();


            System.out.println("User OTP deserialized successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred during deserialization: " + e.getMessage());
        }
        return userMailAndOTPSerailzeble;
    }
}

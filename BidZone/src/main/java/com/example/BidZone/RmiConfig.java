package com.example.BidZone;
import com.example.BidZone.repostry.ChatRepoService;
import com.example.BidZone.repostry.MessageRepostory;
import com.example.BidZone.repostry.UserRepository;
import com.example.BidZone.service.MessageServiceImpl;
import com.example.BidZone.util.RealTimeMessageManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Configuration
@Service
public class RmiConfig {
    private static final int RMI_PORT = 4200;

    private final MessageRepostory messageRepository;
    private final UserRepository userRepository;
    private final RealTimeMessageManage realTimeMessageManage;

    private Registry registry;

    @Autowired
    public RmiConfig(MessageRepostory messageRepository, UserRepository userRepository, RealTimeMessageManage realTimeMessageManage) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.realTimeMessageManage = realTimeMessageManage;
    }

    @Bean
    public ChatRepoService chatService() throws RemoteException {
        try {
            if (registry == null) {
                registry = LocateRegistry.createRegistry(RMI_PORT);
            }

            ChatRepoService chatService = new MessageServiceImpl(messageRepository, userRepository, realTimeMessageManage);
            registry.rebind("ChatService", chatService);
            System.out.println("RMI service successfully created.");
            return chatService;
        } catch (Exception e) {
            System.err.println("Failed to create and bind RMI service: " + e.getMessage());
            throw e;
        }
    }

}

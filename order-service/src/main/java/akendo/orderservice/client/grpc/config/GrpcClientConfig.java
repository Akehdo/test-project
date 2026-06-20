package akendo.orderservice.client.grpc.config;

import akendo.grpc.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel userServiceChannel(
            @Value("${grpc.user-service.host:localhost}") String host,
            @Value("${grpc.user-service.port:9090}") int port
    ) {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(
            ManagedChannel userServiceChannel
    ) {
        return UserServiceGrpc.newBlockingStub(userServiceChannel);
    }
}

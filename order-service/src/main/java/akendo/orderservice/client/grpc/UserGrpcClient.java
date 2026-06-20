package akendo.orderservice.client.grpc;

import akendo.grpc.user.CheckUserRequest;
import akendo.grpc.user.CheckUserResponse;
import akendo.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {
    private final UserServiceGrpc.UserServiceBlockingStub userStub;

    public boolean checkUserExists(String userID) {
        CheckUserRequest request = CheckUserRequest.newBuilder()
                .setUserId(userID)
                .build();

        CheckUserResponse response = userStub.checkUserExists(request);

        return response.getExists();
    }

}
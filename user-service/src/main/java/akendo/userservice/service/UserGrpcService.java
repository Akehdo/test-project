package akendo.userservice.service;

import akendo.grpc.user.CheckUserRequest;
import akendo.grpc.user.CheckUserResponse;
import akendo.grpc.user.UserServiceGrpc;
import akendo.userservice.repository.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase{
    private final UserRepository userRepository;

    @Override
    public void checkUserExists(
            CheckUserRequest request,
            StreamObserver<CheckUserResponse> responseObserver
    ) {
        UUID userId;
        try {
             userId = UUID.fromString(request.getUserId());
        } catch (IllegalArgumentException exception) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid user ID")
                            .asRuntimeException()
            );
            return;
        }


        boolean exists = userRepository.existsById(userId);

        CheckUserResponse response = CheckUserResponse.newBuilder()
                .setExists(exists)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

package com.management.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

  public BillingServiceGrpcClient(
      @Value("${billing.service.address:localhost}") String serverAddress,
      @Value("${billing.service.grpc.port:9001}") int port) {
      log.info("Connecting to GRPC Billing Service at {}:{}", serverAddress, port);

      ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

      blockingStub = BillingServiceGrpc.newBlockingStub(channel);


  }

  public BillingResponse createBillingAccount(String patientId, String name, String email, String address) {
    BillingRequest request =
        BillingRequest.newBuilder().setPatientId(patientId).setName(name).setEmail(email).setAddress(address).build();

    BillingResponse response = blockingStub.createBillingAccount(request);

    log.info("Received response from GRPC billing service: {}", response.toString());

    return response;
  }
}

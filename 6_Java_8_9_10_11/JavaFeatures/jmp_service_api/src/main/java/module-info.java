import org.example.serviceApi.Service;

module jmp_service_api {
    uses Service;

    requires jmp_dto;

    exports org.example.serviceApi;
}
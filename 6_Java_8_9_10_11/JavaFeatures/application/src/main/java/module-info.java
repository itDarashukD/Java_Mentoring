module application {
    uses org.example.api.Bank;
    uses org.example.serviceApi.Service;

    requires jmp_dto;
    requires jmp_bank_api;
    requires jmp_service_api;
    requires jmp_cloud_bank_impl;
    requires jmp_cloud_service_impl;
}

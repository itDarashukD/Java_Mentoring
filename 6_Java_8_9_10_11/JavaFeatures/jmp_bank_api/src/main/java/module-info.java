import org.example.api.Bank;

module jmp_bank_api {
    uses Bank;

    requires jmp_dto;

    exports org.example.api;

}
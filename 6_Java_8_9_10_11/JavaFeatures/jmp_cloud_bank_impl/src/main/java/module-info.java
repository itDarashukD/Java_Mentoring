import org.example.api.Bank;
import org.example.bank.BankImpl;


module jmp_cloud_bank_impl {
    requires jmp_dto;
    requires jmp_bank_api;

    exports org.example.bank;

    provides Bank with BankImpl;

}
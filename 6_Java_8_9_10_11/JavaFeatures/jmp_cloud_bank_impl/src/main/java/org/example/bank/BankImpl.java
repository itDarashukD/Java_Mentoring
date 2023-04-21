package org.example.bank;

import java.util.HashMap;
import java.util.Map;
import org.example.dto.User;
import org.example.api.Bank;
import org.example.dto.BankCard;
import org.example.dto.BankCardType;
import org.example.dto.CreditBankCard;
import org.example.dto.DebitBankCard;

public class BankImpl implements Bank {

    @Override
    public BankCard createBankCard(User user, BankCardType type) {
        var bankCardMap = createBankCardMap(user);

        return bankCardMap.get(type);

    }

    private Map<BankCardType, BankCard> createBankCardMap(User user) {
        Map<BankCardType, BankCard> bankCard = new HashMap<>();
        bankCard.put(BankCardType.CREDIT, new CreditBankCard("1111", user));
        bankCard.put(BankCardType.DEBIT, new DebitBankCard("2222", user));

        return bankCard;

    }

}

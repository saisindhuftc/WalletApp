package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterWalletTransaction;
import com.example.walletapplication.entity.IntraWalletTransaction;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.repository.InterWalletTransactionRepository;
import com.example.walletapplication.repository.IntraWalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private IntraWalletTransactionRepository intraWalletTransactionRepository;

    @Autowired
    private InterWalletTransactionRepository interWalletTransactionRepository;

    @Transactional(readOnly = true)
    public List<Object> getTransactions(Long userId, Long walletId, String sortBy, String sortOrder, String transactionType) {
        if (isUserAuthorized(userId, walletId)) {
            if (transactionType == null) {
                throw new IllegalArgumentException("Transaction type cannot be null");
            }
            List<String> sortOrderList = sortOrder != null ? Arrays.asList(sortOrder.split(",")) : Collections.emptyList();
            List<String> sortByList = sortBy != null ? Arrays.asList(sortBy.split(",")) : Collections.emptyList();
            List<String> transactionTypeList = transactionType != null ? Arrays.asList(transactionType.split(",")) : Collections.emptyList();
            validateSortParameters(sortByList, sortOrderList, transactionTypeList);

            List<IntraWalletTransaction> intraTransactions = intraWalletTransactionRepository.findByWalletIdAndDate(walletId, LocalDateTime.now().minusDays(30), LocalDateTime.now());
            List<InterWalletTransaction> senderTransactions = interWalletTransactionRepository.findBySenderWalletIdAndDate(walletId, LocalDateTime.now().minusDays(30), LocalDateTime.now());
            List<InterWalletTransaction> receiverTransactions = interWalletTransactionRepository.findByReceiverWalletIdAndDate(walletId, LocalDateTime.now().minusDays(30), LocalDateTime.now());

            List<Object> allTransactions = new ArrayList<>();
            allTransactions.addAll(intraTransactions);
            allTransactions.addAll(senderTransactions);
            allTransactions.addAll(receiverTransactions);

            if (transactionType != null) {
                allTransactions = allTransactions.stream()
                        .filter(transaction -> transactionTypeList.contains(getTransactionType(transaction)))
                        .collect(Collectors.toList());
            }

            Comparator<Object> comparator = getComparatorObject(sortOrderList, sortByList);
            allTransactions.sort(comparator);
            return allTransactions;
        }
        return Collections.emptyList();
    }

    private void validateSortParameters(List<String> sortByList, List<String> sortOrderList, List<String> transactionTypeList) {
        if (sortByList.size() < sortOrderList.size()) {
            throw new IllegalArgumentException("Sort order list cannot be greater than sort by list");
        }

        // Define valid fields, orders, and transaction types as constants
        final List<String> validSortFields = Arrays.asList("timestamp", "amount");
        final List<String> validSortOrders = Arrays.asList("ASC", "DESC");
        final List<String> validTransactionTypes = Arrays.asList("WITHDRAW", "DEPOSIT", "TRANSFER");

        // Validate sort fields
        validateListElements(sortByList, validSortFields, "Invalid sort field");

        // Validate sort orders
        validateListElements(sortOrderList, validSortOrders, "Invalid sort order");

        // Validate transaction types
        validateListElements(transactionTypeList, validTransactionTypes, "Invalid transaction type");
    }

    private void validateListElements(List<String> elements, List<String> validElements, String errorMessagePrefix) {
        for (String element : elements) {
            if (!validElements.contains(element.toUpperCase())) {
                throw new IllegalArgumentException(errorMessagePrefix + ": " + element);
            }
        }
    }

    private Comparator<Object> getComparatorObject(List<String> sortOrderList, List<String> sortByList) {
        Comparator<Object> comparator = Comparator.comparing((Object o) -> {
            if (o instanceof IntraWalletTransaction) {
                return ((IntraWalletTransaction) o).getTimestamp();
            } else if (o instanceof InterWalletTransaction) {
                return ((InterWalletTransaction) o).getTimestamp();
            }
            return null;
        });

        if (sortByList.contains("amount")) {
            comparator = Comparator.comparing((Object o) -> {
                if (o instanceof IntraWalletTransaction) {
                    return ((IntraWalletTransaction) o).getAmount();
                } else if (o instanceof InterWalletTransaction) {
                    return ((InterWalletTransaction) o).getAmount();
                }
                return null;
            });
        }

        if (sortOrderList.contains("DESC")) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private boolean isUserAuthorized(Long userId, Long walletId) {
        return true;
    }

    private String getTransactionType(Object transaction) {
        if (transaction instanceof IntraWalletTransaction) {
            return ((IntraWalletTransaction) transaction).getType().name();
        } else if (transaction instanceof InterWalletTransaction) {
            return ((InterWalletTransaction) transaction).getTransactionType().name();
        }
        return null;
    }
}
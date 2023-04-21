package com.example.concurrency.currency.dao.impl;

import com.example.concurrency.currency.dao.ExchangeDao;
import com.example.concurrency.currency.model.Currency;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExchangeDaoImpl implements ExchangeDao {


    @Value("${path.to.file}")
    private String pathToFile;

    public static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger log = LoggerFactory.getLogger(ExchangeDaoImpl.class);

    @Override
    public Optional<BigDecimal> readSumToExchangeFromFile(Currency currency) {
        lock.readLock().lock();

        String fileName = String.format("%s.txt", currency);
        String pathToReadFile = pathToFile + fileName;

        log.info("Reading file... : {}", pathToReadFile);

        try {
	   Path path = Paths.get(pathToReadFile);

	   if (!isOneLineInFile(path)) {
	       throw new IllegalArgumentException("The file include to much lines! ");
	   }

	   return Files.lines(path)
		  .map(string -> BigDecimal.valueOf(Double.parseDouble(string)))
		  .findFirst();
        } catch (IOException e) {
	   throw new RuntimeException(e);
        } finally {
	   log.info("Reading file : {} successfully finished ",pathToFile);
	   lock.readLock().unlock();
        }
    }

    @Override
    public File writeExchangedSumToFile(Currency currency, BigDecimal exchangeResulSum) {
        lock.writeLock().lock();

        String fileName = String.format("%s.txt", currency);
        String pathToWriteFile = pathToFile + fileName;

        if (!clearFile(pathToWriteFile)) {
	   throw new IllegalStateException("Can't save currency count, because file is not clear");
        }
        log.info("Writing to file... : {}",pathToWriteFile);
        File file;

        try {
	   Path path = Paths.get(pathToWriteFile);
	   Path write = Files.write(path, String.valueOf(exchangeResulSum).getBytes());
	   file = write.toFile();
        } catch (IOException e) {
	   throw new RuntimeException(e);
        } finally {
	   log.info("Writing to file successfully finished : {}",pathToWriteFile);
	   lock.writeLock().unlock();
        }
        return file;
    }

    private Boolean clearFile(String pathToFile) {
        lock.writeLock().lock();

        log.info("Cleaning file... : {}", pathToFile);

        try {
	   Path path = Paths.get(pathToFile);
	   final BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
	   bufferedWriter.write("");
	   bufferedWriter.flush();

	   return path.toFile().length() == 0;
        } catch (IOException e) {
	   throw new RuntimeException(e);
        } finally {
	   log.info("Cleaning file : {} successfully finished ",pathToFile);
	   lock.writeLock().unlock();
        }
    }

    private Boolean isOneLineInFile(Path path) {
        try {
	   return Files.lines(path).count() == 1;
        } catch (IOException e) {
	   throw new RuntimeException(e);
        }
    }


}
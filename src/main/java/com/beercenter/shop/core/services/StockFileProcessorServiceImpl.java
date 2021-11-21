package com.beercenter.shop.core.services;

import com.beercenter.shop.core.beans.ConfigProperties;
import com.beercenter.shop.core.model.InventoryLevel;
import com.beercenter.shop.core.model.ProductStockInfo;
import com.beercenter.shop.core.model.Variant;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.beercenter.shop.core.utils.InventoryPolicy.ONLY_AT_STORE;
import static com.beercenter.shop.core.utils.InventoryPolicy.REGULAR;

@AllArgsConstructor
@Slf4j
@Service
public class StockFileProcessorServiceImpl {


    private final ConfigProperties configProperties;
    private final ProductServiceImpl productServiceImpl;
    private StringBuilder stringBuilder;
    private Set<Variant> variantUpdateFails;

    public void processFile(final Path filePath) throws FileNotFoundException {
        cleanVariables();
        try {
            log.info("START -> Execution");
            final Set<Variant> variants = getProductsVariants();
            final Map<String, Long> productsStockMap = getProductsStockMapFromFile(filePath);
            final List<Variant> variantsToUpdate = getVariantsToUpdate(variants, productsStockMap);
            if (!CollectionUtils.isEmpty(variantsToUpdate)){
                updateProductsStock(variantsToUpdate);
                updateProductsInventoryPolicy(variantsToUpdate);
            }
            log.info(String.format("END -> Execution { TOTAL READ : %s , TOTAL UPDATED : %s }", productsStockMap.size(), variantsToUpdate.size()));
            addInfo("TOTAL PRODUCTS READ: " + productsStockMap.size());
            addInfo("TOTAL PRODUCTS TO UPDATE: " + variantsToUpdate.size());
            addInfo("TOTAL PRODUCTS UPDATE FAILS: " + variantUpdateFails.size());
            addInfo("FAILS: " + variantUpdateFails.toString());
            writeReport();
        } catch (final Exception e) {
            addInfo("ERROR: " + ExceptionUtils.getMessage(e));
            log.error("Error has occurred while file processing: ", e);
        } finally {
            removeProcessedFile(filePath);
        }
    }

    private void removeProcessedFile(final Path filePath) {

        filePath.toFile().delete();
    }

    private void writeReport() throws IOException {
        final File reportDir = new File(configProperties.getReportfolder());
        final List<Long> foldersNames = Arrays.asList(reportDir.list()).stream().map(folderName -> Long.valueOf(folderName)).collect(Collectors.toList());
        Long max = CollectionUtils.isEmpty(foldersNames)? 0L : Collections.max(foldersNames);
        max += 1;
        final String newReportDirPath = configProperties.getReportfolder() + "\\" + max;
        final File newReportDir = new File(newReportDirPath);
        newReportDir.mkdir();
        final FileWriter myWriter = new FileWriter(newReportDirPath + "\\report.txt");
        myWriter.write(stringBuilder.toString());
        myWriter.close();
    }

    private void addInfo(String message) {
        stringBuilder.append('\n');
        stringBuilder.append(message);
    }

    private void cleanVariables() {
        stringBuilder = new StringBuilder();
        variantUpdateFails = new HashSet<>();
    }

    private Set<Variant> getProductsVariants() {
        log.info("START -> Get products from online store");
        final Set<Variant> variants = productServiceImpl.getShopProducts();
        log.info("END -> Get products from online store");
        return variants;
    }

    private void updateProductsInventoryPolicy(final List<Variant> variantsToUpdate) {
        log.info("START -> Updating products policy");
        variantsToUpdate.forEach(variant -> {
            try {
                productServiceImpl.updateProductVariant(Variant.builder().inventory_policy(variant.getInventory_policy()).id(variant.getId()).build());
            } catch (Exception e) {
                final String message = "ERROR UPDATING POLICY OF PRODUCT " + variant.getSku();
                log.info(message);
                addInfo(message);
                variantUpdateFails.add(variant);
            }
        });
        log.info("END -> Updating products policy");
    }

    private void updateProductsStock(final List<Variant> variantsToUpdate) {
        log.info("START -> Updating products stock");
        final Map<Long, InventoryLevel> inventoryLevelMap = productServiceImpl.getVariantInventoryList(variantsToUpdate);

        variantsToUpdate.forEach(variant -> {
            try {
                productServiceImpl.adjustVariantInventory(inventoryLevelMap.get(variant.getInventory_item_id()), variant.getInventory_quantity());
            } catch (Exception e) {
                final String message = "ERROR UPDATING STOCK OF PRODUCT " + variant.getSku();
                log.info(message);
                addInfo(message);
                variantUpdateFails.add(variant);
            }
        });
        log.info("END -> Updating products stock");
    }

    private List<Variant> getVariantsToUpdate(Set<Variant> variants, Map<String, Long> productsStockMap) {
        final List<Variant> variantsToUpdate = new ArrayList<>();
        log.info("START -> Get products that need to be updated");
        variants.stream().forEach(variant -> {
            final Long stock = productsStockMap.get(variant.getSku());
            if (stock != null) {
                if (variant.getInventory_quantity().compareTo(stock) != 0) {
                    if (stock > 2 || stock <= 0) {
                        variant.setInventory_policy(REGULAR.getValue());
                    } else {
                        variant.setInventory_policy(ONLY_AT_STORE.getValue());
                    }
                    variant.setInventory_quantity(stock - variant.getInventory_quantity());
                    variantsToUpdate.add(variant);
                }
            } else {
                log.info("Variant not found in the inventory file: {}", variant);
            }
        });
        log.info("END -> Get products that need to be updated");
        return variantsToUpdate;
    }

    private Map<String, Long> getProductsStockMapFromFile(final Path filePath) throws IOException {
        log.info("START -> Reading products from file");
        final FileReader fileReader = new FileReader(filePath.toFile());
        List<ProductStockInfo> productStockInfoList = new CsvToBeanBuilder(fileReader).withSkipLines(1)
                .withType(ProductStockInfo.class).withSeparator(';').build().parse();
        fileReader.close();
        log.info("END -> Reading products from file");

        return productStockInfoList.stream().collect(Collectors.toMap(ProductStockInfo::getSku, ProductStockInfo::getStock));
    }
}

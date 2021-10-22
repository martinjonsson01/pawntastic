package com.thebois.models.world.structures;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;

public class StockpileTests {

    @BeforeEach
    public void setFactoryInventory() {
        final Inventory sharedInventory = new Inventory();
        StructureFactory.setInventory(sharedInventory);
    }

    @AfterEach
    public void setFactoryInventoryToNull() {
        StructureFactory.setInventory(null);
    }

    @Test
    public void getCostReturnsNonZeroWhenBuilt() {
        // Arrange
        final IStructure stockpile =
            StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);

        final Collection<ItemType> neededItemTypes = stockpile.getNeededItems();
        final List<IItem> neededItems =
            neededItemTypes.stream().map(ItemFactory::fromType).collect(Collectors.toList());
        neededItems.forEach(stockpile::tryDeliverItem);

        // Act
        final float cost = stockpile.getCost();

        // Assert
        assertThat(cost).isNotZero();
    }

    @Test
    public void getInventoryReturnsSharedInventory() {
        // Arrange
        final Inventory sharedInventory = new Inventory();

        // Act
        StructureFactory.setInventory(sharedInventory);
        final Stockpile stockpile1 =
            (Stockpile) StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);
        final Stockpile stockpile2 =
            (Stockpile) StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);

        final IInventory stockpileInventory1 = stockpile1.getInventory();
        final IInventory stockpileInventory2 = stockpile2.getInventory();

        // Assert
        assertThat(stockpileInventory1).isEqualTo(sharedInventory);
        assertThat(stockpileInventory2).isEqualTo(sharedInventory);
    }

    @Test
    public void stockpileNeededItemsAreTheSameAsGetBuildMaterials() {
        // Arrange
        final Stockpile stockpile =
            (Stockpile) StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);
        final ItemType[] neededItems = new ItemType[8];
        for (int i = 0; i < 4; i++) {
            neededItems[i] = ItemType.LOG;
        }
        for (int i = 4; i < 8; i++) {
            neededItems[i] = ItemType.ROCK;
        }

        // Act
        final ItemType[] stockpileNeededItems = stockpile.getNeededItems().toArray(new ItemType[8]);

        // Assert
        assertThat(stockpileNeededItems).isEqualTo(neededItems);
    }

}

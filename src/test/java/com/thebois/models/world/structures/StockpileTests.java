package com.thebois.models.world.structures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void getCostIsHigh() {
        // Arrange
        final Stockpile stockpile =
            (Stockpile) StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);

        // Act
        final float cost = stockpile.getCost();

        // Assert
        assertThat(cost).isEqualTo(Float.MAX_VALUE);
    }

    @Test
    public void deepCloneReturnsCopyOfStockpile() {
        // Arrange
        final Stockpile stockpile =
            (Stockpile) StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);

        // Act
        final Stockpile copy = (Stockpile) stockpile.deepClone();

        // Assert
        assertThat(copy).isNotEqualTo(stockpile);
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

    @Test
    public void stockpileDelegatesToInventoryForCanItemFitMethod() {
        // Arrange
        final IItem item = mock(IItem.class);
        when(item.getType()).thenReturn(ItemType.ROCK);
        final IInventory inventory = mock(Inventory.class);
        StructureFactory.setInventory(inventory);
        final IStructure stockpile = StructureFactory.createStructure(StructureType.STOCKPILE,
                                                                      0,
                                                                      0);

        // Act
        stockpile.canFitItem(item.getType());

        // Assert
        verify(inventory, times(1)).canFitItem(item.getType());
    }

    @Test
    public void stockpileDelegatesToInventoryForGiveItemMethod() {
        // Arrange
        final IInventory inventory = mock(Inventory.class);
        StructureFactory.setInventory(inventory);
        final IStructure stockpile = StructureFactory.createStructure(StructureType.STOCKPILE,
                                                                      0,
                                                                      0);

        // Act
        stockpile.addItem(any());

        // Assert
        verify(inventory, times(1)).tryAdd(any());
    }

    @Test
    public void stockpileDelegatesToInventoryForHasItemMethod() {
        // Arrange
        final IInventory inventory = mock(Inventory.class);
        StructureFactory.setInventory(inventory);
        final IStructure stockpile = StructureFactory.createStructure(StructureType.STOCKPILE,
                                                                      0,
                                                                      0);

        // Act
        stockpile.hasItem(any());

        // Assert
        verify(inventory, times(1)).hasItem(any());
    }

    @Test
    public void stockpileDelegatesToInventoryForTakeItemMethod() {
        // Arrange
        final IInventory inventory = mock(Inventory.class);
        StructureFactory.setInventory(inventory);
        final IStructure stockpile = StructureFactory.createStructure(StructureType.STOCKPILE,
                                                                      0,
                                                                      0);

        // Act
        stockpile.takeItem(any());

        // Assert
        verify(inventory, times(1)).take(any());
    }

}

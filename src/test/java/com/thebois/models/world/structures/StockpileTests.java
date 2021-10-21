package com.thebois.models.world.structures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockpileTests {

    private final IInventory inventory = mock(IInventory.class);

    @BeforeEach
    public void setFactoryInventory() {
        StructureFactory.setInventory(inventory);
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

    private IStructure createStockpile() {
        return StructureFactory.createStructure(StructureType.STOCKPILE, 0, 0);
    }

    @Test
    public void stockpileDelegatesTryAddToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final IStoreable stockpile = (IStoreable) structure;

        // Act
        stockpile.tryAdd(any());

        // Assert
        verify(inventory, times(1)).tryAdd(any());
    }

    @Test
    public void stockpileDelegatesAddMultipleToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final IStoreable stockpile = (IStoreable) structure;

        // Act
        stockpile.addMultiple(any());

        // Assert
        verify(inventory, times(1)).addMultiple(any());
    }

    @Test
    public void stockpileDelegatesCanFitItemToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final IStoreable stockpile = (IStoreable) structure;

        // Act
        stockpile.canFitItem(any());

        // Assert
        verify(inventory, times(1)).canFitItem(any());
    }

    @Test
    public void stockpileDelegatesIsFullToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final IStoreable stockpile = (IStoreable) structure;

        // Act
        stockpile.isFull();

        // Assert
        verify(inventory, times(1)).isFull();
    }

    @Test
    public void stockpileDelegatesHasItemToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.hasItem(any());

        // Assert
        verify(inventory, times(1)).hasItem(any());
    }

    @Test
    public void stockpileDelegatesHasItemAmountToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.hasItem(any(), anyInt());

        // Assert
        verify(inventory, times(1)).hasItem(any(), anyInt());
    }

    @Test
    public void stockpileDelegatesNumberOfToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.numberOf(any());

        // Assert
        verify(inventory, times(1)).numberOf(any());
    }

    @Test
    public void stockpileDelegatesTakeToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.take(any());

        // Assert
        verify(inventory, times(1)).take(any());
    }

    @Test
    public void stockpileDelegatesTakeAmountToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.takeAmount(any(), anyInt());

        // Assert
        verify(inventory, times(1)).takeAmount(any(), anyInt());
    }

    @Test
    public void stockpileDelegatesTakeNextItemToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.takeNextItem();

        // Assert
        verify(inventory, times(1)).takeNextItem();
    }

    @Test
    public void stockpileDelegatesIsEmptyToInventory() {
        // Arrange
        final IStructure structure = createStockpile();
        final ITakeable stockpile = (ITakeable) structure;

        // Act
        stockpile.isEmpty();

        // Assert
        verify(inventory, times(1)).isEmpty();
    }

}

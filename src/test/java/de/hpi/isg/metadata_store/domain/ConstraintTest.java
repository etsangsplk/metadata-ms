package de.hpi.isg.metadata_store.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import de.hpi.isg.metadata_store.domain.common.Observer;
import de.hpi.isg.metadata_store.domain.constraints.impl.TypeConstraint;
import de.hpi.isg.metadata_store.domain.constraints.impl.TypeConstraint.TYPES;
import de.hpi.isg.metadata_store.domain.impl.DefaultMetadataStore;
import de.hpi.isg.metadata_store.domain.impl.SingleTargetReference;
import de.hpi.isg.metadata_store.domain.location.impl.IndexedLocation;
import de.hpi.isg.metadata_store.domain.targets.Column;
import de.hpi.isg.metadata_store.domain.targets.Schema;
import de.hpi.isg.metadata_store.domain.targets.Table;
import de.hpi.isg.metadata_store.domain.targets.impl.DefaultColumn;
import de.hpi.isg.metadata_store.domain.targets.impl.DefaultSchema;
import de.hpi.isg.metadata_store.domain.targets.impl.DefaultTable;
import de.hpi.isg.metadata_store.exceptions.NotAllTargetsInStoreException;

public class ConstraintTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddingConstraintsToUnmodifiableConstraintCollectionFails() {

        final MetadataStore store1 = new DefaultMetadataStore();

        final Column dummyColumn = DefaultColumn.buildAndRegister(store1, mock(Table.class), "dummyColumn1",
                new IndexedLocation(0, null));

        final Constraint dummyTypeContraint = new TypeConstraint(1, new SingleTargetReference(dummyColumn),
                TYPES.STRING, mock(ConstraintCollection.class));

        store1.getConstraints().add(dummyTypeContraint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingATypeConstraintOnSchemasFails() {

        final MetadataStore store1 = new DefaultMetadataStore();

        final Schema dummySchema = DefaultSchema.buildAndRegister(store1, "dummySchema", mock(Location.class));

        new TypeConstraint(1, new SingleTargetReference(dummySchema), TYPES.STRING, mock(ConstraintCollection.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingATypeConstraintOnTablesFails() {

        final MetadataStore store1 = new DefaultMetadataStore();

        final Table dummyTable = DefaultTable.buildAndRegister(store1, mock(Schema.class), "dummySchema",
                mock(Location.class));

        new TypeConstraint(1, new SingleTargetReference(dummyTable), TYPES.STRING, mock(ConstraintCollection.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingATypeConstraintWithTakenIdFails() {

        final MetadataStore store1 = new DefaultMetadataStore();

        final Table dummyTable = DefaultTable.buildAndRegister(store1, mock(Schema.class), "dummySchema",
                mock(Location.class));

        new TypeConstraint(1, new SingleTargetReference(dummyTable), TYPES.STRING, mock(ConstraintCollection.class));

    }

    @Test
    public void testTypeConstraint() {

        final Column dummyColumn = DefaultColumn.buildAndRegister(mock(MetadataStore.class), mock(Table.class),
                "dummyColumn1", new IndexedLocation(0, null));

        final ConstraintCollection cC = mock(ConstraintCollection.class);
        final Constraint dummyTypeContraint1 = new TypeConstraint(1,
                new SingleTargetReference(dummyColumn), TYPES.STRING, cC);
        final Constraint dummyTypeContraint2 = new TypeConstraint(1,
                new SingleTargetReference(dummyColumn), TYPES.STRING, cC);

        assertEquals(dummyTypeContraint1, dummyTypeContraint2);
    }

    @Test
    public void testTypeConstraintOnAddedColumn() {

        final MetadataStore store = new DefaultMetadataStore();

        final Column dummyColumn = DefaultColumn.buildAndRegister(store, mock(Table.class), "dummyColumn3 ",
                new IndexedLocation(0, null));

        store.getSchemas().add(
                DefaultSchema.buildAndRegister(store, "dummySchema", null).addTable(
                        DefaultTable.buildAndRegister(store, mock(Schema.class), "dummyTable", null).addColumn(
                                dummyColumn)));

        final Constraint dummyTypeContraint = new TypeConstraint(1, new SingleTargetReference(dummyColumn),
                TYPES.STRING, mock(ConstraintCollection.class));

        store.addConstraint(dummyTypeContraint);
    }

    @Test(expected = NotAllTargetsInStoreException.class)
    public void testTypeConstraintOnNotAddedColumnFails() {

        final MetadataStore store2 = new DefaultMetadataStore();

        final Column dummyColumn = DefaultColumn.buildAndRegister(mock(Observer.class), mock(Table.class),
                "dummyColumn2", new IndexedLocation(0, null));

        final Constraint dummyTypeContraint = new TypeConstraint(1, new SingleTargetReference(dummyColumn),
                TYPES.STRING, mock(ConstraintCollection.class));

        store2.addConstraint(dummyTypeContraint);
    }

}

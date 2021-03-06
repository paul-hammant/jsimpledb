
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.jsck;

import org.jsimpledb.core.ObjId;
import org.jsimpledb.schema.SetSchemaField;
import org.jsimpledb.util.ByteReader;
import org.jsimpledb.util.ByteWriter;

class SetElementIndex extends CollectionElementIndex {

    SetElementIndex(JsckInfo info, int schemaVersion, SetSchemaField field) {
        super(info, schemaVersion, field, "set");
    }

    @Override
    protected void validateIndexEntrySuffix(JsckInfo info, ByteReader reader, byte[] indexValue, ObjId id) {

        // No additional info
        this.validateEOF(reader);

        // Validate element exists in set
        if (info.getConfig().isRepair()) {
            final ByteWriter writer = this.buildFieldKey(id, this.parentStorageId);
            writer.write(indexValue);
            final byte[] key = writer.getBytes();
            if (info.getKVStore().get(key) == null) {
                throw new IllegalArgumentException("object " + id + " set field #" + this.parentStorageId
                  + " does not contain value " + Jsck.ds(indexValue) + " having " + this.type);
            }
        }
    }
}


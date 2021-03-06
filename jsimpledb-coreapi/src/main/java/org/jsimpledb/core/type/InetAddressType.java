
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.core.type;

import com.google.common.base.Preconditions;

import java.net.InetAddress;

import org.jsimpledb.util.ByteReader;
import org.jsimpledb.util.ByteWriter;

/**
 * Non-null {@link InetAddress} type. Null values are not supported by this class.
 */
public class InetAddressType extends AbstractInetAddressType<InetAddress> {

    private static final long serialVersionUID = 3568938922398348273L;

    private static final byte PREFIX_IPV4 = 4;
    private static final byte PREFIX_IPV6 = 6;

    public InetAddressType() {
        super(InetAddress.class, "(" + Inet4AddressType.PATTERN + "|" + Inet6AddressType.PATTERN + ")");
    }

// FieldType

    @Override
    public boolean hasPrefix0x00() {
        return false;
    }

    @Override
    public boolean hasPrefix0xff() {
        return false;
    }

    @Override
    protected int getLength(ByteReader reader) {
        switch (reader.readByte()) {
        case PREFIX_IPV4:
            return 4;
        case PREFIX_IPV6:
            return 16;
        default:
            throw new IllegalArgumentException("invalid encoded InetAddress");
        }
    }

    @Override
    public void write(ByteWriter writer, InetAddress addr) {
        Preconditions.checkArgument(writer != null);
        Preconditions.checkArgument(addr != null);
        final byte[] bytes = addr.getAddress();
        switch (bytes.length) {
        case 4:
            writer.writeByte(PREFIX_IPV4);
            break;
        case 16:
            writer.writeByte(PREFIX_IPV6);
            break;
        default:
            throw new RuntimeException("internal error");
        }
        writer.write(bytes);
    }
}


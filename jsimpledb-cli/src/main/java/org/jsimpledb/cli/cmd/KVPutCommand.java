
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.cli.cmd;

import java.util.EnumSet;
import java.util.Map;

import org.jsimpledb.Session;
import org.jsimpledb.SessionMode;
import org.jsimpledb.cli.CliSession;
import org.jsimpledb.util.ParseContext;

public class KVPutCommand extends AbstractKVCommand {

    public KVPutCommand() {
        super("kvput key:bytes value:bytes");
    }

    @Override
    public String getHelpSummary() {
        return "Write a raw database key/value pair";
    }

    @Override
    public String getHelpDetail() {
        return "Writes a raw database key/value pair. The `key' and `value' parameters may be given as a hexadecimal value"
          + " or C-style doubly-quoted string."
          + "\n\nWARNING: this command can corrupt a JSimpleDB database.";
    }

    @Override
    public EnumSet<SessionMode> getSessionModes() {
        return EnumSet.of(SessionMode.KEY_VALUE);
    }

    @Override
    public CliSession.Action getAction(CliSession session, ParseContext ctx, boolean complete, Map<String, Object> params) {
        return new PutAction((byte[])params.get("key"), (byte[])params.get("value"));
    }

    private static class PutAction implements CliSession.Action, Session.RetryableAction {

        private final byte[] key;
        private final byte[] value;

        PutAction(byte[] key, byte[] value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void run(CliSession session) throws Exception {
            session.getKVTransaction().put(this.key, this.value);
        }
    }
}


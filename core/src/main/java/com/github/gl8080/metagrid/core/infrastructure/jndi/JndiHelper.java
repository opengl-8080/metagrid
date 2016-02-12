package com.github.gl8080.metagrid.core.infrastructure.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.github.gl8080.metagrid.core.MetaGridException;

public class JndiHelper {
    
    @SuppressWarnings("unchecked")
    public <T> T lookup(String key) {
        try {
            Context ctx = new InitialContext();
            return (T)ctx.lookup(key);
        } catch (NamingException e) {
            throw new MetaGridException("JNDI ルックアップに失敗しました。", e);
        }
    }
}

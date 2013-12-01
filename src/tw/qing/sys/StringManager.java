/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 From Tomcat,  org.apache.catalina.tribes.util.StringManager
 */

package tw.qing.sys;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class StringManager
{
    private static Logger log = Logger.getLogger(StringManager.class);
    private ResourceBundle bundle;

    private StringManager(String packageName)
    {
        bundle = ResourceBundle.getBundle(packageName);
    }

    public String getString(String key)
    {
        if (key == null)
        {
            String msg = "key is null";
            throw new NullPointerException(msg);
        }

        String str = null;

        try
        {
            str = bundle.getString(key);
        } catch (MissingResourceException mre)
        {
            log.warn("cannot find the key '" + key + "' in the resource bundle");
        }
        return str;
    }

    public String getString(String key, Object[] args)
    {
        String iString = null;
        String value = getString(key);

        // this check for the runtime exception is some pre 1.1.6
        // VM's don't do an automatic toString() on the passed in
        // objects and barf out

        try
        {
            // ensure the arguments are not null so pre 1.2 VM's don't barf
            Object nonNullArgs[] = args;
            for (int i = 0; i < args.length; i++)
            {
                if (args[i] == null)
                {
                    if (nonNullArgs == args)
                        nonNullArgs = (Object[]) args.clone();
                    nonNullArgs[i] = "null";
                }
            }
            iString = MessageFormat.format(value, nonNullArgs);
        } catch (IllegalArgumentException iae)
        {
            StringBuffer buf = new StringBuffer();
            buf.append(value);
            for (int i = 0; i < args.length; i++)
            {
                buf.append(" arg[" + i + "]=" + args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }

    public String getString(String key, Object arg)
    {
        Object[] args = new Object[]
        { arg };
        return getString(key, args);
    }

    public String getString(String key, Object arg1, Object arg2)
    {
        Object[] args = new Object[]
        { arg1, arg2 };
        return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it with the
     * given object arguments. These arguments can of course be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     * @param arg3
     */

    public String getString(String key, Object arg1, Object arg2, Object arg3)
    {
        Object[] args = new Object[]
        { arg1, arg2, arg3 };
        return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it with the
     * given object arguments. These arguments can of course be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */

    public String getString(String key, Object arg1, Object arg2, Object arg3, Object arg4)
    {
        Object[] args = new Object[]
        { arg1, arg2, arg3, arg4 };
        return getString(key, args);
    }

    // --------------------------------------------------------------
    // STATIC SUPPORT METHODS
    // --------------------------------------------------------------

    private static Hashtable managers = new Hashtable();

    /**
     * Get the StringManager for a particular package. If a manager for a
     * package already exists, it will be reused, else a new StringManager will
     * be created and returned.
     */
    public synchronized static StringManager getManager(String packageName)
    {
        StringManager mgr = (StringManager) managers.get(packageName);

        if (mgr == null)
        {
            mgr = new StringManager(packageName);
            managers.put(packageName, mgr);
        }
        return mgr;
    }
}

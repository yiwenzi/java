package com.joker.export;

import org.apache.commons_voltpatches.cli.*;

import javax.net.ssl.SSLContext;
import java.io.Console;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by hunter on 2017/12/26.
 */
public abstract class CLIConfig {

    @Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
    @Target({ElementType.FIELD})        // This annotation can only be applied to class methods.
    public @interface Option {
        String opt() default "";

        String shortOpt() default "";

        boolean hasArg() default true;

        boolean required() default false;

        String desc() default "";
    }

    @Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
    @Target({ElementType.FIELD})       // This annotation can only be applied to class methods.
    public @interface AdditionalArgs {
        String opt() default "";

        boolean hasArg() default false;

        boolean required() default false;

        String desc() default "";
    }

    //存着所有的参数
    protected final Options options = new Options();
    protected Options helpmsgs = new Options();
    protected String cmdName = "command";

    protected String configDump;
    protected String usage;
    protected SSLContext m_sslContext;

    public void setSSLContext(SSLContext sslContext) {
        m_sslContext = sslContext;
    }

    public SSLContext getSSLContext() {
        return m_sslContext;
    }

    public void exitWithMessageAndUsage(String msg) {
        System.err.println(msg);
        printUsage();
        System.exit(-1);
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdName, helpmsgs, false);
    }

    private void assignValueToField(Field field, String value) throws IllegalAccessException {
        if (value == null || (value.length() == 0))
            return;
        field.setAccessible(true);
        Class<?> cls = field.getType();
        if ((cls == boolean.class) || (cls == Boolean.class)) {
            field.set(this, Boolean.parseBoolean(value));
        } else if ((cls == byte.class) || (cls == Byte.class)) {
            field.set(this, Byte.parseByte(value));
        } else if ((cls == short.class) || (cls == Short.class)) {
            field.set(this, Short.parseShort(value));
        } else if ((cls == int.class) || (cls == Integer.class)) {
            field.set(this, Integer.parseInt(value));
        } else if ((cls == long.class) || (cls == Long.class)) {
            field.set(this, Long.parseLong(value));
        } else if ((cls == float.class) || (cls == Float.class)) {
            field.set(this, Float.parseFloat(value));
        } else if ((cls == double.class) || (cls == Double.class)) {
            field.set(this, Double.parseDouble(value));
        } else if ((cls == String.class)) {
            field.set(this, value);
        } else if (value.length() == 1 && ((cls == char.class) || (cls == Character.class))) {
            field.set(this, value.charAt(0));
        } else {
            System.err.println("Parsing failed. Reason: can not assign " + value + " to "
                    + cls.toString() + " class");
            printUsage();
            System.exit(-1);
        }
    }

    //从注解中为字段赋值
    private void addOptionFromField(Field field) {
        if (field.isAnnotationPresent(Option.class)) {
            Option option = field.getAnnotation(Option.class);
            String opt = option.opt(); //就是字段名
            if ((opt == null) || (opt.trim().length() == 0)) {
                opt = field.getName();
            }
            String shortOpt = option.shortOpt();
            if ((shortOpt == null) || (shortOpt.trim().length() == 0)) {
                options.addOption(null, opt, option.hasArg(), option.desc());
                helpmsgs.addOption(null, opt, option.hasArg(), option.desc());
            } else {
                options.addOption(shortOpt, opt, option.hasArg(), option.desc());
                helpmsgs.addOption(shortOpt, opt, option.hasArg(), option.desc());
            }
        } else if (field.isAnnotationPresent(AdditionalArgs.class)) {
            AdditionalArgs params = field.getAnnotation(AdditionalArgs.class);
            String opt = params.opt();
            if ((opt == null) || (opt.trim().length() == 0)) {
                opt = field.getName();
            }
            options.addOption(opt, params.hasArg(), params.desc());
        }
    }

    public void parse(String cmdName, String[] args) {
        this.cmdName = cmdName;
        try {
            options.addOption("help", "h", false, "Print this message");
            List<Field> allFields = getFields(getClass());
            //先为注解赋值，这是不对的，要先取得所有字段的注解值，key是字段名或字段名的缩写
            allFields.forEach(this::addOptionFromField);

            CommandLineParser parser = new PosixParser();
            //这里是重点-- 结合options和入参来为字段赋值
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                printUsage();
                System.exit(0);
            }
            String[] leftargs = cmd.getArgs();
            int leftover = 0;
            Map<String, String> kvMap = new TreeMap<String, String>();
            Field[] fields = new Field[allFields.size()];
            int n = 0;
            //这里才是为字段赋值
            for (Field field : allFields) {
                fields[n++] = field;
                if (field.isAnnotationPresent(AdditionalArgs.class)) {
                    leftover++;
                    continue;
                }
                if (field.isAnnotationPresent(Option.class)) {
                    Option option = field.getAnnotation(Option.class);
                    String opt = option.opt();
                    if ((opt == null) || (opt.trim().length() == 0)) {
                        opt = field.getName();
                    }
                    if (cmd.hasOption(opt)) {
                        if (option.hasArg()) { // 注解中存在该字段的值，若字段本身就有值呢？
                            assignValueToField(field, cmd.getOptionValue(opt));
                        } else {
                            if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                                field.setAccessible(true);
                                try {
                                    field.set(this, true);
                                } catch (Exception e) {
                                    throw new IllegalArgumentException(e);
                                }
                            } else {
                                printUsage();
                            }
                        }
                    } else {
                        if (option.required()) {
                            printUsage();
                        }
                    }

                    field.setAccessible(true);
                    kvMap.put(opt, field.get(this).toString());
                }
            }
            if (leftargs != null) {
                if (leftargs.length < leftover) {
                    for (int i = 0, j = 0; i < leftargs.length; i++) {
                        for (; j < fields.length; j++) {
                            if (fields[j].isAnnotationPresent(AdditionalArgs.class)) {
                                break;
                            }
                        }
                        fields[j].setAccessible(true);
                        fields[j].set(this, leftargs[i]);
                    }
                } else {
                    System.err.println("Expected " + leftover + " args, but receive " + leftargs.length + " args");
                    printUsage();
                    System.exit(-1);
                }
            }

            // check that the values read are valid
            // this code is specific to your app
            validate();

            //build a debug string
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : kvMap.entrySet()) {
                sb.append(e.getKey()).append(" = ").append(e.getValue()).append("\n");
            }
            configDump = sb.toString();
        } catch (Exception e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            printUsage();
            System.exit(-1);
        }
    }

    public static List<Field> getFields(Class<?> startClass) {
        List<Field> currentClassFields = new ArrayList<>();
        currentClassFields.addAll(Arrays.asList(startClass.getDeclaredFields()));
        Class<?> parentClass = startClass.getSuperclass();
        if (parentClass != null) {
            List<Field> parentClassField = getFields(parentClass);
            currentClassFields.addAll(parentClassField);
        }
        return currentClassFields;
    }

    public static String readPasswordIfNeeded(String user, String pwd, String prompt) throws IOException {
        // If a username was specified, and no password was specified,
        // then read password from the console
        if((null != user) && (user.trim().length() > 0)) {
            if ((pwd == null) || (pwd.trim().length() == 0)) {
                Console console = System.console();
                if(console == null) {
                    throw new IOException("Unable to read password from console.");
                }
                char[] val = console.readPassword("%s", prompt);
                if(val != null) {
                    return new String(val);
                } else {
                    // Allow the user to omit the password, let the authentication
                    // logic handle password validation
                    return "";
                }
            }
        }
        return pwd;
    }

    public void validate() {
    }

    public String getConfigDumpString() {
        return configDump;
    }
}

package com.ms.karorkefz.xposed.mysqlclient;

import android.app.AlertDialog;
import android.util.Log;

import com.ms.karorkefz.util.Log.LogUtil;

import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.ms.karorkefz.util.Shot.getActivity;

public class mysqlclient {
    ClassLoader classLoader;
    public mysqlclient(ClassLoader classLoader ) {
        this.classLoader = classLoader;
    }

    public void init() {
        Log.v( "karorkefz", "进入mysqlclient" );
//        XposedBridge.hookAllMethods( ClassLoader.class, "loadClass", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                if (param.hasThrowable()) return;
//                if (param.args.length != 1) return;
//                Class<?> cls = (Class<?>) param.getResult();
//                String name = cls.getName();
//                Log.v( "mysqlclient", name );
//                if (name.equals( "com.vcrox.mysqlclient.EncodeDecodeAES" )) {
                    Log.v( "mysqlclient", "mysqlclient-准备加载" );
                    XposedHelpers.findAndHookMethod( "com.vcrox.mysqlclient.EncodeDecodeAES",
                            classLoader,
                            "getRawKey",// 被Hook的函数
                            byte[].class,
                            new XC_MethodReplacement() {
                                /**
                                 * Shortcut for replacing a method completely. Whatever is returned/thrown here is taken
                                 * instead of the result of the original method (which will not be called).
                                 *
                                 * <p>Note that implementations shouldn't call {@code super(param)}, it's not necessary.
                                 *
                                 * @param param Information about the method call.
                                 * @throws Throwable Anything that is thrown by the callback will be passed on to the original caller.
                                 */
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
//                                    SecureRandom sr = null;
//                                    if (android.os.Build.VERSION.SDK_INT >= 17) {
//                                        sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
//                                    } else {
//                                        sr = SecureRandom.getInstance("SHA1PRNG");
//                                    }
//                                    sr.setSeed( (byte[]) param.args[0] );
//                                    kgen.init(128, sr); // 192 and 256 bits may not be available
//                                    SecretKey skey = kgen.generateKey();
//                                    byte[] raw = skey.getEncoded();
                                    Log.e( "mysqlclient", "mysqlclient-准备加载" );
//                                    Log.e( "mysqlclient", "mysqlclient-准备加载"+ param.args[0] );
//                                    String password= (String) param.args[0];
                                    byte[] passwordBytes = "1".getBytes( StandardCharsets.US_ASCII);
                                    return new SecretKeySpec(InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, 128), "AES");
                                }
                            } );
//                }
//            }
//        } );
    }

    static final class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}

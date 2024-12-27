package com.vicious.persist;

import com.vicious.persist.annotations.C_NAME;
import com.vicious.persist.mappify.ClassToName;
import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Persist {
    public static final Logger logger = LogManager.getLogManager().getLogger("persist");

    public static void doC_NAMEScan(){
        AnnotationDetector.TypeReporter reporter = new AnnotationDetector.TypeReporter(){
            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{C_NAME.class};
            }

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
                if(annotation == C_NAME.class){
                    try {
                        ClassToName.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        final AnnotationDetector detector = new AnnotationDetector(reporter);
        try {
            detector.detect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

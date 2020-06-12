package com.trevormetcalf.schoolscheduler.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/*
    This class is used to serialize and deserialize objects.
    Takes an object and then turns it into a string.
    The serialized object can be passed as a String Extra between activities.
    The string can then be de-serialized or converted back into the object at the destination.
    Provides an easy way to move data from terms, courses, assessments, mentors, notes and
    assessment notes between activities.
 */

public final class Serializer {

    public static String serialize(Object obj) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(obj);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static Object deserialize(String serial) {
        try {
            byte b[] = Base64.getDecoder().decode(serial.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}

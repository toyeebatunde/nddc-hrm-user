package co.payrail.attendance_srv.auth.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneNumberUtil {
    private static final int PHONE_NO_LENGTH = 14;
    public static String validateNumber(String phone) throws NullPointerException {
        if (phone == null) {
            throw new NullPointerException("Phone number is null");
        }

        String returned = null;
        String phoneLocal = phone.replaceAll("[()]", "").trim();

        // Check if it is in international format
        if (phoneLocal.contains("+")) {
            // Only send to mobile numbers
            // Check if this is a Nigerian number
            if (phoneLocal.contains("+234")) {
                if (phoneLocal.length() == PHONE_NO_LENGTH) {
                    returned = phoneLocal;
                } else {
                    // This is a landline or invalid number hence no alert will be sent
                    log.error("This is an invalid number or a landline hence no alert will be sent: %s", phoneLocal);
                    log.info("Accepted formats; +234********** and 0**********");
                    throw new NullPointerException();
                }
            } else {
                // A non-Nigerian number
                returned = phoneLocal;
            }

            // Check for more than one + sign
            if (phoneLocal.lastIndexOf('+') > 0) {
                // The phone number contains more than one + therefore it is invalid
                log.error(String.format("The phone number %s has more than one plus", phoneLocal));
                log.info("Accepted formats; +234********** and 0**********");
                throw new NullPointerException("Invalid phone number");
            }
        } else if (phoneLocal.startsWith("234")) {
            log.info("Accepted formats; +234********** and 0**********");
            // Check if it is a landline
            if (phoneLocal.length() < 13) {
                log.error("This is a landline hence no alert will be sent");
                throw new NullPointerException("");
            } else {
                // It is a mobile number
                // Convert it to international format
                returned = "+".concat(phoneLocal.trim());
                log.info("Returned Accepted formats; {}", returned);
            }
        } else {
            // Check if it is a landline
            if (phoneLocal.length() < 10) {
                log.error("This is a landline hence no alert will be sent");
                throw new NullPointerException("");
            } else if (phoneLocal.replace("+", "").length() > 11) {
                log.error("Invalid number: " + phoneLocal);
                log.info("Accepted formats; +234********** and 0**********");
                throw new NullPointerException();
            } else {
                // It is a mobile number
                // Convert it to international format
                returned = "+234" + phoneLocal.substring(1).replaceAll("[()]", "").trim();
            }
        }

        // Check if the number is invalid
        if (returned.length() < PHONE_NO_LENGTH) {
            log.error(String.format("Phone Number %s: is invalid", phoneLocal));
            log.info("Accepted formats; +234********** and 0**********");
            throw new NullPointerException("");
        } else if (returned.equals("+2348000000000")) {
            log.error("Invalid number: 08000000000");
            throw new NullPointerException("");
        }

        return returned;
    }
}

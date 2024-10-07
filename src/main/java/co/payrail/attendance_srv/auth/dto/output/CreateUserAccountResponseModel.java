package co.payrail.attendance_srv.auth.dto.output;

import co.payrail.attendance_srv.auth.dto.input.UserDto;
import co.payrail.attendance_srv.auth.entity.User;

public class CreateUserAccountResponseModel {
    private String otp;
    private String otpExpirationTimeInMinutes;
    private String error;
    private UserDto userDto;

    public String getOtp() {
        return otp;
    }

    public CreateUserAccountResponseModel setOtp(String otp) {
        this.otp = otp;
        return this;
    }

    public CreateUserAccountResponseModel setOtpExpirationTimeInMinutes(String otpExpirationTime) {
        this.otpExpirationTimeInMinutes = otpExpirationTime;
        return this;
    }

    public String getError() {
        return error;
    }

    public CreateUserAccountResponseModel setError(String error) {
        this.error = error;
        return this;
    }



    public String getOtpExpirationTimeInMinutes() {
        return otpExpirationTimeInMinutes;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public CreateUserAccountResponseModel setUserDto(UserDto userDto) {
        this.userDto = userDto;
        return this;
    }
}

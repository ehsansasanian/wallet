package com.example.ewallet.domain;

import javax.persistence.*;

@Entity
public class UserPic {

    @Id
    private Long phoneNum;

    private Byte[] picture;

    public UserPic() {

    }

    public UserPic(Long userId, Byte[] pic) {
        this.phoneNum = userId;
        this.picture = pic;
    }
}

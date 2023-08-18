package com.zam.sidik_padang.roodiskusi;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseApiResponse;
import com.zam.sidik_padang.util.User;

public class LoginApiResponse extends BaseApiResponse {
    public List<User> member = new ArrayList<>();
}

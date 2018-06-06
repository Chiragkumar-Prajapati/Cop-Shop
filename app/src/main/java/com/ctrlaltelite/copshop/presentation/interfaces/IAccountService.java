package com.ctrlaltelite.copshop.presentation.interfaces;

import com.ctrlaltelite.copshop.objects.AccountObject;

public interface IAccountService {
    public AccountObject validateUsernameAndPassword (String username, String password);
}

package com.muc;

//TODO brues til LIST protocol
public interface UserStatusListener {
    void online(String login);
    void offline(String login);
}

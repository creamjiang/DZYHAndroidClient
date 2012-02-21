package com.dld.coupon.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dld.android.net.Param;
import com.dld.android.persistent.SharePersistent;
import com.dld.android.util.LogUtils;
import com.dld.coupon.util.Common;
import com.dld.coupon.util.LetterUtil;
import com.dld.coupon.util.StringUtils;
import com.dld.coupon.util.ToastUtil;
import com.dld.coupon.view.DownloadListView;
import com.dld.coupon.view.PromptDialog;
import com.dld.coupon.view.DownloadListView.DownLoadAdapter;
import com.dld.protocol.json.CProtocol;
import com.dld.protocol.json.CommonResponse;
import com.dld.protocol.json.Protocol;
import com.dld.protocol.json.RegisteredContact;
import com.dld.protocol.json.RegisteredReponse;
import com.dld.protocol.json.Protocol.OnJsonProtocolResult;
import com.dld.coupon.activity.R;
import com.tencent.weibo.utils.Cache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class InviteActivity extends BaseActivity {
    List<Contact> contacts = new ArrayList();
    DownloadListView.DownLoadAdapter contactsAdapter;
    Map<String, String> contactsByPhone = new HashMap();
    DownloadListView contactslListView;
    Handler handler = new Handler();
    DownloadListView.DownLoadAdapter invitedAdapter;
    DownloadListView invitedListView;
    Set<String> invitedPhones;
    String phone;
    Button phoneButton;
    List<Contact> registered = new ArrayList();

    private void bind(Contact paramContact, String paramString) {
        this.handler.post(new Runnable() {
            public void run() {
                ToastUtil.showMsg("绑定手机号的请求已发送，请稍候...");
            }
        });
        CProtocol localCProtocol = new CProtocol(this, null, "bind_phone", null);
        Cache.remove(localCProtocol.getAbsoluteUrl());
        Param localParam = new Param().append("cid",
                SharePersistent.get("customer_id")).append("mobile",
                paramString);
        localCProtocol.startTransForUser(new BindPhoneResult(paramString,
                paramContact, true), localParam);
    }

    private void init() {
        this.invitedPhones = StringUtils.stringToStringSet(
                SharePersistent.get("invited_mobiles"), ",");
        initPhoneButton();
        readContacts();
    }

    private void initPhoneButton() {
        String str = SharePersistent.get("customer_id");
        this.phoneButton = ((Button) findViewById(R.id.phone));
        this.phone = SharePersistent.getPhone(str);
        if (!StringUtils.isEmpty(this.phone))
            resetPhone();
        this.phoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                InviteActivity.this.showBindPhoneDialog(null);
            }
        });
    }

    private void invite(Contact paramContact) {
        ToastUtil.showMsg("邀请好友的请求已发送，请稍候...");
        CProtocol localCProtocol = new CProtocol(this, null, "invite", null);
        Param localParam = new Param()
                .append("cid", SharePersistent.get("customer_id"))
                .append("ivrm", this.phone).append("ive", paramContact.name)
                .append("ivem", paramContact.tel);
        String str = (String) this.contactsByPhone.get(this.phone);
        if (!StringUtils.isEmpty(str))
            localParam.append("ivr", str);
        Cache.remove(localCProtocol.getAbsoluteUrl());
        localCProtocol.startTransForUser(new InviteResult(paramContact.tel),
                localParam);
        this.invitedPhones.add(paramContact.tel);
        SharePersistent.set("invited_mobiles",
                StringUtils.toString(this.invitedPhones, ","));
        this.handler.post(new Runnable() {
            public void run() {
                InviteActivity.this.contactsAdapter.notifyDownloadBack();
            }
        });
    }

    private void readContacts() {
        this.contactslListView = ((DownloadListView) findViewById(R.id.contacts));
        this.contactsAdapter = new ContactsAdapter();
        this.contactslListView.setAdapter(this.contactsAdapter);
        this.contactslListView.reset();
        this.contacts.clear();
        this.contactsByPhone.clear();
        new Thread() {
            private String getColumn(Cursor paramCursor, String paramString) {
                return paramCursor.getString(paramCursor
                        .getColumnIndex(paramString));
            }

            public void run() {
                Object localObject3 = InviteActivity.this.getContentResolver();
                Object localObject4 = ((ContentResolver) localObject3).query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, null);
                Object localObject1 = new HashMap();
                String str1;
                Object localObject2;
                if (localObject4 != null) {
                    while (((Cursor) localObject4).moveToNext()) {
                        str1 = getColumn((Cursor) localObject4, "data1");
                        if (!StringUtils.checkPhone(str1))
                            continue;
                        if (str1.length() > 11)
                            str1 = str1.substring(-11 + str1.length(),
                                    str1.length());
                        String str2 = getColumn((Cursor) localObject4,
                                "contact_id");
                        localObject2 = (List) ((Map) localObject1).get(str2);
                        if (localObject2 == null) {
                            localObject2 = new ArrayList();
                            ((Map) localObject1).put(str2, localObject2);
                        }
                        ((List) localObject2).add(str1);
                    }
                    ((Cursor) localObject4).close();
                }
                if (!((Map) localObject1).isEmpty()) {
                    localObject2 = ((ContentResolver) localObject3).query(
                            ContactsContract.Contacts.CONTENT_URI, null, null,
                            null, null);
                    if (localObject2 != null) {
                        while (((Cursor) localObject2).moveToNext()) {
                            localObject3 = (List) ((Map) localObject1)
                                    .get(getColumn((Cursor) localObject2, "_id"));
                            if ((localObject3 == null)
                                    || (((List) localObject3).isEmpty()))
                                continue;
                            localObject4 = ((List) localObject3).iterator();
                            while (((Iterator) localObject4).hasNext()) {
                                str1 = (String) ((Iterator) localObject4)
                                        .next();
                                localObject3 = getColumn((Cursor) localObject2,
                                        "display_name");
                                InviteActivity.this.contactsByPhone.put(str1,
                                        (String) localObject3);
                            }
                        }
                        ((Cursor) localObject2).close();
                        localObject1 = InviteActivity.this.contactsByPhone
                                .entrySet().iterator();
                        while (((Iterator) localObject1).hasNext()) {
                            localObject2 = (Map.Entry) ((Iterator) localObject1)
                                    .next();
                            InviteActivity.this.contacts
                                    .add(new InviteActivity.Contact(
                                            (String) ((Map.Entry) localObject2)
                                                    .getValue(),
                                            (String) ((Map.Entry) localObject2)
                                                    .getKey()));
                        }
                        Collections.sort(InviteActivity.this.contacts);
                        Cache.put("contacts", InviteActivity.this.contacts);
                    }
                } else {
                    InviteActivity.this.handler.post(new Runnable() {
                        public void run() {
                            InviteActivity.this.contactsAdapter = new InviteActivity.ContactsAdapter();
                            InviteActivity.this.contactslListView
                                    .setAdapter(InviteActivity.this.contactsAdapter);
                            if (!InviteActivity.this.contacts.isEmpty())
                                InviteActivity.this.contactsAdapter
                                        .notifyDownloadBack();
                            else
                                InviteActivity.this.contactsAdapter
                                        .notifyNoResult();
                        }
                    });
                    InviteActivity.this.startReadRegistered();
                }
            }
        }.start();
    }

    private void resetPhone() {
        this.phoneButton.setText("我的号码（" + this.phone + "）");
        this.phoneButton.setBackgroundResource(R.drawable.green_button_long);
    }

    private void showBindPhoneDialog(final Contact paramContact) {
        EditText localEditText = new EditText(this);
        localEditText.setInputType(3);
        if (!StringUtils.isEmpty(this.phone)) {
            localEditText.setText(this.phone);
            localEditText.setSelection(this.phone.length());
        }
        new PromptDialog(this, "请输入您的电话号码", this.phone) {
            public void onOkClicked(View paramView) {
                Common.hideKeyboard(paramView);
                String str = ((TextView) paramView).getText().toString();
                if (!StringUtils.equals(str, InviteActivity.this.phone))
                    if (StringUtils.checkPhone(str)) {
                        if (str.length() > 11)
                            str = str.substring(-11 + str.length(),
                                    str.length());
                        InviteActivity.this.bind(paramContact, str);
                    } else {
                        ToastUtil.showMsg("手机号码格式错误");
                    }
            }
        };
    }

    private void showRegistered() {
        if (!this.registered.isEmpty()) {
            findViewById(R.id.invited_text).setVisibility(View.VISIBLE);
            findViewById(R.id.invited_layout).setVisibility(View.VISIBLE);
            if (this.invitedListView == null) {
                this.invitedListView = ((DownloadListView) findViewById(R.id.invited));
                this.invitedAdapter = new ContactsAdapter();
                this.invitedListView.setAdapter(this.invitedAdapter);
                this.invitedListView
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(
                                    AdapterView<?> paramAdapterView,
                                    View paramView, int paramInt, long paramLong) {
                                Cache.put("contact",
                                        InviteActivity.this.registered
                                                .get(paramInt));
                                Common.startActivity(FriendActivity.class);
                            }
                        });
            }
            this.contacts.removeAll(this.registered);
            this.invitedAdapter.notifyDownloadBack();
            this.contactsAdapter.notifyDownloadBack();
        }
    }

    private void startReadRegistered() {
        if (!this.contacts.isEmpty()) {
            Object localObject = StringUtils.toString(this.contacts, ",");
            CProtocol localCProtocol = new CProtocol(this, null,
                    "registered_contacts", null);
            Cache.remove(localCProtocol.getAbsoluteUrl());
            localObject = new Param().append("m", (String) localObject);
            localCProtocol.startTransForUser(new ReadRegisteredResult(),
                    (Param) localObject);
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.invite);
        init();
        initSegment();
    }

    class BindPhoneResult extends Protocol.OnJsonProtocolResult {
        private boolean bind;
        private InviteActivity.Contact contact;
        private String phone;

        public BindPhoneResult(String paramContact,
                InviteActivity.Contact paramBoolean, boolean arg4) {
            super();
            this.phone = paramContact;
            this.contact = paramBoolean;
            // boolean bool;
            this.bind = false;
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.e("test", paramException);
            InviteActivity.this.handler.post(new Runnable() {
                public void run() {
                    if (InviteActivity.BindPhoneResult.this.bind)
                        ToastUtil.showMsg("手机号绑定失败，请稍后重试。");
                }
            });
        }

        public void onResult(String paramString, Object paramObject) {
            CommonResponse localCommonResponse = (CommonResponse) paramObject;
            if (StringUtils.isEmpty(localCommonResponse.message)) {
                if (this.bind)
                    InviteActivity.this.handler.post(new Runnable() {
                        public void run() {
                            SharePersistent.savePhone(
                                    SharePersistent.get("customer_id"),
                                    InviteActivity.BindPhoneResult.this.phone);
                            InviteActivity.this.phone = InviteActivity.BindPhoneResult.this.phone;
                            InviteActivity.this.resetPhone();
                            ToastUtil.showMsg("手机号绑定成功");
                        }
                    });
                if (this.contact != null)
                    InviteActivity.this.handler.post(new Runnable() {
                        public void run() {
                            InviteActivity.this
                                    .invite(InviteActivity.BindPhoneResult.this.contact);
                        }
                    });
            } else {
                onException(paramString, new Exception(
                        localCommonResponse.message));
            }
        }
    }

    public static final class Contact implements Comparable<Contact> {
        public int id;
        public String letters;
        public String name;
        public String tel;

        public Contact() {
        }

        Contact(int paramInt, String paramString1, String paramString2) {
            this(paramString1, paramString2);
            this.id = paramInt;
        }

        Contact(String paramString1, String paramString2) {
            this.name = paramString1;
            this.tel = paramString2;
            this.letters = LetterUtil.getLetters(paramString1).toUpperCase();
        }

        public int compareTo(Contact paramContact) {
            int i = 0;
            try {
                i = this.letters.compareTo(paramContact.letters);
                i = i;
                return i;
            } catch (Exception j) {
                // while (true)
                {
                    LogUtils.e("test", j);
                    // int j = 0;
                }
            }
            return i;
        }

        public boolean equals(Object paramObject) {
            boolean bool = false;
            try {
                Contact localContact = (Contact) paramObject;
                bool = StringUtils.equals(this.tel, localContact.tel);
                bool = bool;
                return bool;
            } catch (Exception i) {
                // while (true)
                {
                    LogUtils.e("test", i);
                    // int i = 0;
                }
            }
            return bool;
        }

        public int hashCode() {
            return this.tel.hashCode();
        }

        public String toString() {
            return this.tel;
        }
    }

    class ContactsAdapter extends DownloadListView.DownLoadAdapter {
        List<InviteActivity.Contact> bean;

        public ContactsAdapter() {
            // Object localObject = null;
            // this.bean = (List<Contact>) localObject;
            this.bean = new ArrayList<Contact>();
        }

        public Context getContext() {
            return InviteActivity.this;
        }

        public Object getItem(int paramInt) {
            return this.bean.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public int getListCount() {
            return this.bean.size();
        }

        public int getMax() {
            return this.bean.size();
        }

        public View getView(int paramInt) {
            final InviteActivity.Contact localContact = (InviteActivity.Contact) this.bean
                    .get(paramInt);
            View localView = ((LayoutInflater) getContext().getSystemService(
                    "layout_inflater")).inflate(R.layout.invite_item_layout,
                    null);
            ((TextView) localView.findViewById(R.id.contact_name))
                    .setText(localContact.name);
            ((TextView) localView.findViewById(R.id.contact_tel))
                    .setText(localContact.tel);
            Button localButton = (Button) localView
                    .findViewById(R.id.invite_btn);
            if (this.bean != InviteActivity.this.registered) {
                if (InviteActivity.this.invitedPhones
                        .contains(localContact.tel)) {
                    localButton.setText("再次邀请");
                    localButton.setBackgroundResource(R.drawable.green_button);
                }
                localButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        if (!StringUtils.isEmpty(InviteActivity.this.phone))
                            InviteActivity.this.invite(localContact);
                        else
                            InviteActivity.this
                                    .showBindPhoneDialog(localContact);
                    }
                });
            } else {
                localButton.setVisibility(8);
                localView.findViewById(R.id.jt).setVisibility(View.VISIBLE);
            }
            return localView;
        }

        public void onNotifyDownload() {
            InviteActivity.this.readContacts();
        }
    }

    class InviteResult extends Protocol.OnJsonProtocolResult {
        String phone;

        public InviteResult(String arg2) {
            super();
            Object localObject;
            this.phone = new String();
        }

        public void onException(String paramString, Exception paramException) {
            InviteActivity.this.handler.post(new Runnable() {
                public void run() {
                    ToastUtil.showMsg("邀请失败，请稍后重试。");
                }
            });
        }

        public void onResult(String paramString, final Object paramObject) {
            InviteActivity.this.handler.post(new Runnable() {
                public void run() {
                    CommonResponse localCommonResponse = (CommonResponse) paramObject;
                    if (!StringUtils.isEmpty(localCommonResponse.message))
                        ToastUtil.showMsg(localCommonResponse.message);
                    else
                        ToastUtil.showMsg("邀请短信发送成功。");
                }
            });
        }
    }

    class ReadRegisteredResult extends Protocol.OnJsonProtocolResult {
        public ReadRegisteredResult() {
            super();
        }

        public void onException(String paramString, Exception paramException) {
            LogUtils.e("test", paramException);
        }

        public void onResult(String paramString, Object paramObject) {
            RegisteredReponse localRegisteredReponse = (RegisteredReponse) paramObject;
            if (StringUtils.isEmpty(localRegisteredReponse.message))
                InviteActivity.this.handler.post(new Runnable() {
                    public void run() {
                        Iterator localIterator = InviteActivity.this.contacts
                                .iterator();
                        while (localIterator.hasNext()) {
                            RegisteredContact localRegisteredContact = (RegisteredContact) localIterator
                                    .next();
                            String str1 = localRegisteredContact.mobile;
                            String str2 = (String) InviteActivity.this.contactsByPhone
                                    .get(str1);
                            if (StringUtils.isEmpty(str2))
                                continue;
                            int i = localRegisteredContact.id;
                            if (i <= 0)
                                continue;
                            InviteActivity.this.registered
                                    .add(new InviteActivity.Contact(i, str2,
                                            str1));
                        }
                        if (!InviteActivity.this.registered.isEmpty()) {
                            Collections.sort(InviteActivity.this.registered);
                            InviteActivity.this.showRegistered();
                        }
                    }
                });
        }
    }
}

package com.sp.lib.widget.lock;

public class NineLock implements ILock {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_OPEN_SUCCESS = 1;
    public static final int STATUS_OPEN_FAILED = 2;

    private char[] RAW_PASSWORD = new char[]{
            '1', '2', '3',
            '4', '5', '6',
            '7', '8', '9'
    };

    private String mDrawSecret = "";
    private int status = STATUS_NORMAL;

    public NineLock() {
        this("");
    }

    public NineLock(String secret) {
        int length = Math.min(secret.length(), RAW_PASSWORD.length);
        for (int i = 0; i < length; i++) {
            appendSecret(secret.charAt(i));
        }
    }

    public void appendPosition(int position) {
        appendSecret(RAW_PASSWORD[position]);
    }

    public void appendSecret(char secret) {

        if (mDrawSecret.length() >= RAW_PASSWORD.length) {
            return;
        }
        if (!contains(RAW_PASSWORD, secret)) {
            return;
        }

        if (contains(mDrawSecret.toCharArray(), secret)) {
            return;
        }
        mDrawSecret += secret;
    }

    /**
     * 补全上一个密码和要添加的密码连线上的密码。如选择1后又连接了9,1和9连线上有个5，则密码变为：159.
     *
     * @param secret
     */
    public void appendFixedSecret(char secret) {
        appendFixedPosition(getPosition(secret));
    }

    public void appendFixedPosition(int position) {

        //如果是第一次添加，就不用补全了
        if (mDrawSecret.length() == 0) {
            appendPosition(position);
            return;
        }

        int lastPosition = getPosition(mDrawSecret.charAt(mDrawSecret.length() - 1));
        int currentPosition = position;

        int lastRow = lastPosition / 3;
        int lastColumn = lastPosition % 3;

        int curRow = currentPosition / 3;
        int curColumn = currentPosition % 3;

        int fixedRow = -1;
        int fixedColumn = -1;

        if (lastRow == curRow) {
            //在同一行
            fixedRow = lastRow;
            if (Math.abs(lastColumn - curColumn) == 2) {
                fixedColumn = (lastColumn + curColumn) / 2;
            }

        } else if (lastColumn == curColumn) {
            //同一列
            fixedColumn = lastColumn;
            if (Math.abs(lastRow - curRow) == 2) {
                fixedRow = (lastRow + curRow) / 2;
            }
        } else if (Math.abs(lastColumn - curColumn) == 2 && Math.abs(lastRow - curRow) == 2) {
            //对角线
            fixedColumn = (lastColumn + curColumn) / 2;
            fixedRow = (lastRow + curRow) / 2;
        }

        if (fixedRow >= 0 && fixedColumn >= 0) {
            appendPosition(fixedRow * 3 + fixedColumn);
        }

        appendPosition(currentPosition);
    }

    public boolean contains(int row, int column) {
        return contains(RAW_PASSWORD[row * 3 + column]);
    }

    public boolean contains(char secret) {
        return contains(mDrawSecret.toCharArray(), secret);
    }

    private boolean contains(char[] array, char c) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            if (array[i] == c) {
                return true;
            }
        }
        return false;
    }

    protected boolean tryUnLock() {
        return false;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public final boolean unLock() {
        if (tryUnLock()) {
            status = STATUS_OPEN_SUCCESS;
            return true;
        } else {
            status = STATUS_OPEN_FAILED;
            return false;
        }
    }

    @Override
    public void reset() {
        mDrawSecret = "";
        status = STATUS_NORMAL;
    }


    public String getDrawSecret() {
        return mDrawSecret;
    }

    /**
     * @return 0-9,或者-1表示不存在
     */
    public int getPosition(char secret) {
        for (int i = 0; i < RAW_PASSWORD.length; i++) {
            if (RAW_PASSWORD[i] == secret) {
                return i;
            }
        }
        return -1;
    }

    public void setSecret(String secret) {
        for (int i = 0; i < secret.length(); i++) {
            appendSecret(secret.charAt(i));
        }
    }
}

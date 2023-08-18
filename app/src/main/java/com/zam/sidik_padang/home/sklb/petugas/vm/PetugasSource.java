package com.zam.sidik_padang.home.sklb.petugas.vm;

/**
 * Created by AbangAzmi on 23/03/2018.
 */

public class PetugasSource {

    private static PetugasSource INSTANCE;

    private PetugasSource() {
    }

    public synchronized static PetugasSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PetugasSource();
        }
        return INSTANCE;
    }

    private boolean isPetugasUpdated = false;
    private boolean isPemilikUpdated = false;

    public boolean isPetugasUpdated() {
        return isPetugasUpdated;
    }

    public void setPetugasUpdated(boolean petugasUpdated) {
        isPetugasUpdated = petugasUpdated;
    }

    public boolean isPemilikUpdated() {
        return isPemilikUpdated;
    }

    public void setPemilikUpdated(boolean pemilikUpdated) {
        isPemilikUpdated = pemilikUpdated;
    }
}
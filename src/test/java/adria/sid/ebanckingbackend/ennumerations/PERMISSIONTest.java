package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PERMISSIONTest {

    @Test
    void testEnumValues() {

        PERMISSION banquierRead = PERMISSION.BANQUIER_READ;
        PERMISSION banquierUpdate = PERMISSION.BANQUIER_UPDATE;
        PERMISSION banquierCreate = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION banquierDelete = PERMISSION.BANQUIER_DELETE;
        PERMISSION clientRead = PERMISSION.CLIENT_READ;
        PERMISSION clientUpdate = PERMISSION.CLIENT_UPDATE;
        PERMISSION clientCreate = PERMISSION.CLIENT_CREATE;
        PERMISSION clientDelete = PERMISSION.CLIENT_DELETE;

        assertNotNull(banquierRead);
        assertNotNull(banquierUpdate);
        assertNotNull(banquierCreate);
        assertNotNull(banquierDelete);
        assertNotNull(clientRead);
        assertNotNull(clientUpdate);
        assertNotNull(clientCreate);
        assertNotNull(clientDelete);
    }

    @Test
    void testEnumPermissions() {
        PERMISSION banquierRead = PERMISSION.BANQUIER_READ;
        PERMISSION banquierUpdate = PERMISSION.BANQUIER_UPDATE;
        PERMISSION banquierCreate = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION banquierDelete = PERMISSION.BANQUIER_DELETE;
        PERMISSION clientRead = PERMISSION.CLIENT_READ;
        PERMISSION clientUpdate = PERMISSION.CLIENT_UPDATE;
        PERMISSION clientCreate = PERMISSION.CLIENT_CREATE;
        PERMISSION clientDelete = PERMISSION.CLIENT_DELETE;

        assertEquals("banquier:read", banquierRead.getPermission());
        assertEquals("banquier:update", banquierUpdate.getPermission());
        assertEquals("banquier:create", banquierCreate.getPermission());
        assertEquals("banquier:delete", banquierDelete.getPermission());
        assertEquals("client:read", clientRead.getPermission());
        assertEquals("client:update", clientUpdate.getPermission());
        assertEquals("client:create", clientCreate.getPermission());
        assertEquals("client:delete", clientDelete.getPermission());
    }

    @Test
    void testEnumToString() {
        PERMISSION banquierRead = PERMISSION.BANQUIER_READ;
        PERMISSION banquierUpdate = PERMISSION.BANQUIER_UPDATE;
        PERMISSION banquierCreate = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION banquierDelete = PERMISSION.BANQUIER_DELETE;
        PERMISSION clientRead = PERMISSION.CLIENT_READ;
        PERMISSION clientUpdate = PERMISSION.CLIENT_UPDATE;
        PERMISSION clientCreate = PERMISSION.CLIENT_CREATE;
        PERMISSION clientDelete = PERMISSION.CLIENT_DELETE;

        assertEquals("BANQUIER_READ", banquierRead.toString());
        assertEquals("BANQUIER_UPDATE", banquierUpdate.toString());
        assertEquals("BANQUIER_CREATE", banquierCreate.toString());
        assertEquals("BANQUIER_DELETE", banquierDelete.toString());
        assertEquals("CLIENT_READ", clientRead.toString());
        assertEquals("CLIENT_UPDATE", clientUpdate.toString());
        assertEquals("CLIENT_CREATE", clientCreate.toString());
        assertEquals("CLIENT_DELETE", clientDelete.toString());
    }

    @Test
    void testEnumValueOf() {
        PERMISSION banquierRead = PERMISSION.valueOf("BANQUIER_READ");
        PERMISSION banquierUpdate = PERMISSION.valueOf("BANQUIER_UPDATE");
        PERMISSION banquierCreate = PERMISSION.valueOf("BANQUIER_CREATE");
        PERMISSION banquierDelete = PERMISSION.valueOf("BANQUIER_DELETE");
        PERMISSION clientRead = PERMISSION.valueOf("CLIENT_READ");
        PERMISSION clientUpdate = PERMISSION.valueOf("CLIENT_UPDATE");
        PERMISSION clientCreate = PERMISSION.valueOf("CLIENT_CREATE");
        PERMISSION clientDelete = PERMISSION.valueOf("CLIENT_DELETE");

        assertEquals(PERMISSION.BANQUIER_READ, banquierRead);
        assertEquals(PERMISSION.BANQUIER_UPDATE, banquierUpdate);
        assertEquals(PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT, banquierCreate);
        assertEquals(PERMISSION.BANQUIER_DELETE, banquierDelete);
        assertEquals(PERMISSION.CLIENT_READ, clientRead);
        assertEquals(PERMISSION.CLIENT_UPDATE, clientUpdate);
        assertEquals(PERMISSION.CLIENT_CREATE, clientCreate);
        assertEquals(PERMISSION.CLIENT_DELETE, clientDelete);
    }

    @Test
    void testEnumOrdinal() {
        PERMISSION banquierRead = PERMISSION.BANQUIER_READ;
        PERMISSION banquierUpdate = PERMISSION.BANQUIER_UPDATE;
        PERMISSION banquierCreate = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION banquierDelete = PERMISSION.BANQUIER_DELETE;
        PERMISSION clientRead = PERMISSION.CLIENT_READ;
        PERMISSION clientUpdate = PERMISSION.CLIENT_UPDATE;
        PERMISSION clientCreate = PERMISSION.CLIENT_CREATE;
        PERMISSION clientDelete = PERMISSION.CLIENT_DELETE;

        assertEquals(0, banquierRead.ordinal());
        assertEquals(1, banquierUpdate.ordinal());
        assertEquals(2, banquierCreate.ordinal());
        assertEquals(3, banquierDelete.ordinal());
        assertEquals(4, clientRead.ordinal());
        assertEquals(5, clientUpdate.ordinal());
        assertEquals(6, clientCreate.ordinal());
        assertEquals(7, clientDelete.ordinal());
    }
}

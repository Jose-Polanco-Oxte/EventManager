package jpolanco.springbootapp.event.application.uc;

import java.util.List;

public interface SearchStaffRolesUC {
    /**
     * Searches for staff roles by name.
     *
     * @param name the name of the staff role to search for.
     * @param size the maximum number of staff roles to return.
     * @return a list of staff roles matching the search criteria.
     */
    List<String> search(String name, int size);
}

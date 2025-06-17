package jpolanco.springbootapp.event.application.ports.output;

import jpolanco.springbootapp.shared.application.adapters.output.CRUDRepository;

import java.util.List;

public interface StaffRolesRepository extends CRUDRepository<String, String> {

    List<String> search(String name, int size);
}

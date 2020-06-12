package hospitalPharmacy.persistence;

/***
 * Interface that determines the behaviour of a repository with CRUD operations
 * @param <ID> - the ID is variable from one Repository to another
 * @param <E> - the type of entities contained in the repository
 */
public interface CrudRepository<ID,E> {
    /***
     * Gives the number of entities contained
     * @return - number of entities
     */
    int size();

    /***
     * saves a entity
     * @param entity - the entity to be added
     */
    void save(E entity);

    /***
     * deletes an element from the repository
     * @param id - the id of the entity to be deleted
     */
    void delete(ID id);

    /***
     * the method updates an entity
     * @param id - the id of the entity to be updated
     * @param entity - the entity of update
     */
    void update(ID id, E entity);

    /***
     * finds a entity given the id
     * @param id - the id of the entity to be found
     * @return - the found entity with the id
     */
    E findOne(ID id);

    /***
     * the method gives a Iterable result consisting of all the entities from the repository
     * @return - all of the entities
     */
    Iterable<E> findAll();

}

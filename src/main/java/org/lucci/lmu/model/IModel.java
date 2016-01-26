package org.lucci.lmu.model;

import java.util.Collection;
import java.util.Set;

/**
 *	This class represents the Model of the app, the core of this package<br/>
 *	It allows you to modify the Model and get a description of its components.
 *
 * 	@author Benjamin Benni
 */
public interface IModel {

	Collection<Collection<Entity>> getAlignments();

	/**
	 * Returns the entities of the Model<br/>
	 * An entity can be a class, an interface, etc.
	 * @return
	 * 		The set of the entities of the Model
	 */
	Set<Entity> getEntities();

	/**
	 * Returns every relations in the Model</br>
	 * A relation link two entities - See Relation class
	 * for more details
	 * @return
	 * 		The set of relations in the Model
	 */
	Set<Relation> getRelations();

	/**
	 * Don't know what this is
	 * @return
	 */
	Collection<Group> getGroups();

	/**
	 * This method allows you to remove an entity previously created in the Model<br/>
	 * This can be useful if you want to analyze a part of the given Model class.
	 * @param entity
	 * 		The entity to remove
	 * @return
	 * 		The collection of the removed entity's relations
	 */
	Collection<? extends Relation> removeEntity(Entity entity);

	/**
	 * This method allows you to add a relation into the Model<br/>
	 * See Relation class for more details.
	 * @param rel
	 * 		The relation to add
	 */
	void addRelation(Relation rel);

	/**
	 * This method allows you to add an Entity into the Model<br/>
	 * See Entity class for more details
	 * @param entity
	 * 		The entity to add
	 */
	void addEntity(Entity entity);
}

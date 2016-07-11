package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This class has been implemented to allow appending on binary file: normally,
 * ObjectOutputStream doesn't allow to append objects to a binary files. Since
 * the board.config file needs to be updated more times, this class allow to
 * correctly write objects in the config file, after it has already been
 * initialized.
 * 
 * @author Riccardo Cannistrà
 *
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {

	public AppendableObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	public void writeStreamHeader() throws IOException {
		reset();
	}
}

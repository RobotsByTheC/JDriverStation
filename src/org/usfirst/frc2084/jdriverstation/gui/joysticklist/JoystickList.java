/*
 * Copyright (c) 2014 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc2084.jdriverstation.gui.joysticklist;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import java.io.IOException;
import net.java.games.input.Controller;

public class JoystickList extends JList {

    static DataFlavor localObjectFlavor;

    static {
        try {
            localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch (ClassNotFoundException cnfe) {
        }
    }
    @SuppressWarnings("StaticNonFinalUsedInInitialization")
    private static final DataFlavor[] supportedFlavors = {localObjectFlavor};
    private final DragSource dragSource;
    private final DropTarget dropTarget;
    private Object dropTargetCell;
    private int draggedIndex = -1;
    private final JoystickListModel model;

    @SuppressWarnings("LeakingThisInConstructor")
    public JoystickList() {
        setModel(model = new JoystickListModel());
        setCellRenderer(new JoystickListCellRenderer());
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_MOVE,
                new JoystickListDragGestureListener());
        dropTarget = new DropTarget(this, new JoystickListDropTargetListener());
    }

    private class JoystickListDragGestureListener implements DragGestureListener {

        @Override
        public void dragGestureRecognized(DragGestureEvent dge) {
            // find object at this x,y
            Point clickPoint = dge.getDragOrigin();
            int index = locationToIndex(clickPoint);
            if (index == -1) {
                return;
            }
            Controller target = model.getElementAt(index);
            Transferable trans = new JoystickTransferable(target);
            draggedIndex = index;
            dragSource.startDrag(dge, Cursor.getDefaultCursor(),
                    trans, new JoystickListDragSourceListener());
        }
    }

    private class JoystickListDragSourceListener extends DragSourceAdapter {

        @Override
        public void dragDropEnd(DragSourceDropEvent dsde) {
            dropTargetCell = null;
            draggedIndex = -1;
            repaint();
        }
    }

    private class JoystickListDropTargetListener extends DropTargetAdapter {

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            if (dtde.getSource() != dropTarget) {
                dtde.rejectDrag();
            } else {
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            }

        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            // figure out which cell it's over, no drag to self
            if (dtde.getSource() != dropTarget) {
                dtde.rejectDrag();
            }
            Point dragPoint = dtde.getLocation();
            int index = locationToIndex(dragPoint);
            if (index == -1) {
                dropTargetCell = null;
            } else {
                dropTargetCell = model.getElementAt(index);
            }
            repaint();
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            if (dtde.getSource() != dropTarget) {
                dtde.rejectDrop();
                return;
            }
            Point dropPoint = dtde.getLocation();
            int index = locationToIndex(dropPoint);
            boolean dropped = false;
            try {
                if ((index == -1) || (index == draggedIndex)) {
                    dtde.rejectDrop();
                    return;
                }
                dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                Object dragged = dtde.getTransferable().getTransferData(localObjectFlavor);
                // move items - note that indicies for insert will
                // change if [removed] source was before target
                boolean sourceBeforeTarget = (draggedIndex < index);
                model.remove(draggedIndex);
                model.add((sourceBeforeTarget ? index - 1 : index), (Controller) dragged);
                dropped = true;
            } catch (UnsupportedFlavorException | IOException e) {
            }
            dtde.dropComplete(dropped);
        }
    }

    class JoystickTransferable implements Transferable {

        private final Controller joystick;

        public JoystickTransferable(Controller c) {
            joystick = c;
        }

        @Override
        public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
            if (isDataFlavorSupported(df)) {
                return joystick;
            } else {
                throw new UnsupportedFlavorException(df);
            }
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor df) {
            return (df.equals(localObjectFlavor));
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return supportedFlavors;
        }
    }

    class JoystickListCellRenderer extends DefaultListCellRenderer {

        boolean isTargetCell;
        boolean isLastItem;
        Insets normalInsets, lastItemInsets;
        int BOTTOM_PAD = 30;

        public JoystickListCellRenderer() {
            super();
            normalInsets = super.getInsets();
            lastItemInsets = new Insets(normalInsets.top,
                    normalInsets.left,
                    normalInsets.bottom + BOTTOM_PAD,
                    normalInsets.right);
        }

        @Override
        public Component getListCellRendererComponent(JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean hasFocus) {
            isTargetCell = (value == dropTargetCell);
            isLastItem = (index == model.getSize() - 1);
            boolean showSelected = isSelected && (dropTargetCell == null);
            return super.getListCellRendererComponent(list, value,
                    index, showSelected,
                    hasFocus);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isTargetCell) {
                g.setColor(Color.black);
                g.drawLine(0, 0, getSize().width, 0);
            }
        }
    }
}

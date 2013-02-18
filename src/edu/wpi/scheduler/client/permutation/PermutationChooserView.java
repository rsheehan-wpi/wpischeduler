package edu.wpi.scheduler.client.permutation;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.SchedulePermutation;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.controller.SectionProducer;
import edu.wpi.scheduler.client.controller.StudentSchedule;

public class PermutationChooserView extends Composite implements ProducerEventHandler {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}

	@UiField(provided = true)
	public PermutationCanvasList thumbList;

	@UiField(provided = true)
	public final PermutationScheduleView scheduleView;

	@UiField
	public VerticalPanel courseList;

	boolean initalized = false;

	private final StudentSchedule studentSchedule;
	public final PermutationController permutationController;

	public PermutationChooserView(StudentSchedule studentSchedule) {
		this.permutationController = new PermutationController(studentSchedule);
		this.studentSchedule = studentSchedule;
		this.thumbList = new PermutationCanvasList(permutationController);
		this.scheduleView = new PermutationScheduleView(permutationController);

		initWidget(uiBinder.createAndBindUi(this));

		courseList.setWidth("100%");

		getElement().getStyle().setLeft(0, Unit.PX);
		getElement().getStyle().setRight(0, Unit.PX);
		getElement().getStyle().setTop(0, Unit.PX);
		getElement().getStyle().setBottom(0, Unit.PX);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	public void updateCourses() {
		courseList.clear();

		for (SectionProducer producer : studentSchedule.sectionProducers) {
			courseList.add(new CourseItem(permutationController, producer));
		}
	}

	public void update() {
		updateCourses();
	}

	@Override
	protected void onLoad() {
		permutationController.addProduceHandler(this);
		update();
	}

	@Override
	protected void onUnload() {
		permutationController.addProduceHandler(this);
	}

	

	@Override
	public void onPermutationUpdated(UpdateType type) {
		List<SchedulePermutation> permutations = permutationController.getProducer().getPermutations();
		
		if( permutations.size() > 0 && permutationController.getSelectedPermutation() == null )
			permutationController.selectPermutation( permutations.get(0) );
	}

	

}

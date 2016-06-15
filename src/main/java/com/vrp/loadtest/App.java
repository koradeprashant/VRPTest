package com.vrp.loadtest;

import java.util.Collection;

import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.VehicleRoutingAlgorithmBuilder;
import jsprit.core.algorithm.state.StateManager;
import jsprit.core.algorithm.termination.IterationWithoutImprovementTermination;
import jsprit.core.problem.Location;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import jsprit.core.problem.constraint.ConstraintManager;
import jsprit.core.problem.io.VrpXMLWriter;
import jsprit.core.problem.job.Service;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.activity.TimeWindow;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.util.Coordinate;
import jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import jsprit.core.util.VehicleRoutingTransportCostsMatrix.Builder;

public class App {
	public static void main(String[] args) {
		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.setFleetSize(FleetSize.FINITE);
		
		//Vehicle 1
		VehicleTypeImpl.Builder typeBuilder01 = VehicleTypeImpl.Builder.newInstance("vehicle-type01")
				.addCapacityDimension(0, 6).addCapacityDimension(1, 0)
				.addCapacityDimension(2, 2100);
		typeBuilder01.setCostPerDistance(1.0);
		typeBuilder01.setCostPerTransportTime(1.0);
		VehicleImpl.Builder vehicleBuilder01 = VehicleImpl.Builder.newInstance("vehicle-01");
		vehicleBuilder01.setStartLocation(Location.newInstance("1"));
		vehicleBuilder01.setType(typeBuilder01.build());
		vehicleBuilder01.setReturnToDepot(false);
		vehicleBuilder01.setEarliestStart(0);
		vehicleBuilder01.setLatestArrival(27201);
		vrpBuilder.addVehicle(vehicleBuilder01.build());
		
		//Vehicle 2
		VehicleTypeImpl.Builder typeBuilder02 = VehicleTypeImpl.Builder.newInstance("vehicle-type02")
				.addCapacityDimension(0, 6).addCapacityDimension(1, 0)
				.addCapacityDimension(2, 1100);
		typeBuilder02.setCostPerDistance(1.0);
		typeBuilder02.setCostPerTransportTime(1.0);
		VehicleImpl.Builder vehicleBuilder02 = VehicleImpl.Builder.newInstance("vehicle-02");
		vehicleBuilder02.setStartLocation(Location.newInstance("1"));
		vehicleBuilder02.setType(typeBuilder02.build());
		vehicleBuilder02.setReturnToDepot(false);
		vehicleBuilder02.setEarliestStart(0);
		vehicleBuilder02.setLatestArrival(27201);
		vrpBuilder.addVehicle(vehicleBuilder02.build());
		
		//Service 1
		Service.Builder builder1 = Service.Builder.newInstance("2").addSizeDimension(0, 1)
				.addSizeDimension(1, 0).addSizeDimension(2, 2059).setServiceTime(120)
				.setLocation(Location.Builder.newInstance().setId("2").setCoordinate(Coordinate.newInstance(28.585054, 77.090116)).build())
				.setTimeWindow(new TimeWindow(26841, 27201));
		vrpBuilder.addJob(builder1.build());
		
		//Service 2
		Service.Builder builder2 = Service.Builder.newInstance("3").addSizeDimension(0, 1)
				.addSizeDimension(1, 0).addSizeDimension(2, 375).setServiceTime(120)
				.setLocation(Location.Builder.newInstance().setId("3").setCoordinate(Coordinate.newInstance(28.7156702, 77.277957)).build())
				.setTimeWindow(new TimeWindow(26841, 27201));
		vrpBuilder.addJob(builder2.build());
		
		// Service 3
		Service.Builder builder3 = Service.Builder.newInstance("4").addSizeDimension(0, 1)
				.addSizeDimension(1, 0).addSizeDimension(2, 323).setServiceTime(120)
				.setLocation(Location.Builder.newInstance().setId("4").setCoordinate(Coordinate.newInstance(28.4956835, 77.1664945)).build())
				.setTimeWindow(new TimeWindow(26841, 27201));
		vrpBuilder.addJob(builder3.build());
		
		VehicleRoutingTransportCostsMatrix.Builder distanceMatrix = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
		setNodeDistances(distanceMatrix);
		vrpBuilder.setRoutingCost(distanceMatrix.build());
		
		VehicleRoutingProblem problem = vrpBuilder.build();
		VehicleRoutingAlgorithmBuilder algoBuilder = new VehicleRoutingAlgorithmBuilder(problem, "algoConfig.xml");
        algoBuilder.addCoreConstraints();
        algoBuilder.addDefaultCostCalculators();

        StateManager stateManager = new StateManager(problem);
        ConstraintManager constraintManager = new ConstraintManager(problem, stateManager);
        
        algoBuilder.setStateAndConstraintManager(stateManager, constraintManager);
        
        VehicleRoutingAlgorithm algorithm = algoBuilder.build();
        algorithm.setPrematureAlgorithmTermination(new IterationWithoutImprovementTermination(100));
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
        
		new VrpXMLWriter(problem, solutions).write("/data/algoOutput.xml");
	}
	
	private static void setNodeDistances(Builder matrix) {
		matrix.addTransportTime(String.valueOf(1), String.valueOf(2), 25970 * 0.003);
		matrix.addTransportDistance(String.valueOf(1), String.valueOf(2), 25970);
		
		matrix.addTransportTime(String.valueOf(1), String.valueOf(3), 20104 * 0.003);
		matrix.addTransportDistance(String.valueOf(1), String.valueOf(3), 20104);
		
		matrix.addTransportTime(String.valueOf(1), String.valueOf(4), 33114 * 0.003);
		matrix.addTransportDistance(String.valueOf(1), String.valueOf(4), 33114);
		
		matrix.addTransportTime(String.valueOf(2), String.valueOf(3), 22523 * 0.003);
		matrix.addTransportDistance(String.valueOf(2), String.valueOf(3), 22523);
		
		matrix.addTransportTime(String.valueOf(2), String.valueOf(4), 11855 * 0.003);
		matrix.addTransportDistance(String.valueOf(2), String.valueOf(4), 11855);
		
		matrix.addTransportTime(String.valueOf(3), String.valueOf(4), 22775 * 0.003);
		matrix.addTransportDistance(String.valueOf(3), String.valueOf(4), 22775);
	}
}

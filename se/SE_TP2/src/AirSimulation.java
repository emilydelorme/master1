
/* AirSimulation class
 *
 * TP2 of SE
 *
 * to be completed...
 *
 */

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class AirSimulation {
	static public int nagents = 4;
	public int nAgent1;
	public int nAgent2;
	public int nAgent3;
	public int nAgent4;
	public Aircraft a;

	public Semaphore[] s;

	// Constructor
	public AirSimulation() {
		this.nAgent1 = 0;
		this.nAgent2 = 0;
		this.nAgent3 = 0;
		this.nAgent4 = 0;
		this.a = new Aircraft();
		this.s = new Semaphore[this.a.getSeatsPerRow()];
		for (int i = 0; i < this.s.length; i++) {
			this.s[i] = new Semaphore(1, true);
		}
	}

	// Agent1 : takes single reservations
	public void agent1() throws InterruptedException {
		// generates a new Customer
		Customer c = new Customer(11 * this.nAgent1 + 1);

		// randomly picks a seat
		Random r = new Random();
		int row = r.nextInt(this.a.getNumberOfRows());
		int col = r.nextInt(this.a.getSeatsPerRow());

		this.s[col].acquire();

		// if the random seat is busy, a free seat is searched
		// (max 100 tries)
		int k = 0;
		while (!this.a.isSeatEmpty(row, col) && k < 100) {
			row = r.nextInt(this.a.getNumberOfRows());
			this.s[col].release();
			col = r.nextInt(this.a.getSeatsPerRow());
			this.s[col].acquire();
			k++;
		}

		// if 100 random tries were not enough, the first available front seat is
		// selected
		boolean found = true;
		if (!this.a.isSeatEmpty(row, col)) {
			this.s[col].release();
			found = false;
			for (int i = 0; !found && i < this.a.getNumberOfRows(); i++) {
				for (int j = 0; !found && j < this.a.getSeatsPerRow(); j++) {
					this.s[j].acquire();
					if (this.a.isSeatEmpty(i, j)) {
						row = i;
						col = j;
						found = true;
					} else {
						this.s[j].release();
					}
				}
			}
		}

		// the Customer is placed at the given seat (if it was found)
		if (found) {
			this.a.add(c, row, col);
			this.nAgent1++;
		}
		this.s[col].release();
	}

	// Agent2: verifies whether Customers requiring assistence are seated
	// on the row with an emergency exit; if yes, the Customer is
	// moved to the closest available seat
	public void agent2() throws InterruptedException {
		boolean found = false;

		// verifying all emergency rows
		ArrayList<Integer> emergencyRows = this.a.getEmergencyRowList();
		for (Integer i : emergencyRows) {
			for (int j = 0; j < this.a.getSeatsPerRow(); j++) {
				this.s[j].acquire();
				if (!this.a.isSeatEmpty(i, j)) {
					Customer c = this.a.getCustomer(i, j);
					if (c.needsAssistence()) {
						// we need to move the Customer: trying the front seats
						found = false;
						int row = i - 1;
						while (!found && row >= 0) {
							int col = 0;
							while (!found && col < this.a.getSeatsPerRow()) {
								if (col != j) {
									this.s[col].acquire();
								}
								if (this.a.isSeatEmpty(row, col)) {
									found = true;
									this.a.freeSeat(i, j);
									this.a.add(c, row, col);
								}
								if (col != j) {
									this.s[col].release();
								}
								col++;
							}
							row--;
						}

						// if no place was found, we try now the back seats
						if (!found) {
							row = i + 1;
							while (!found && row < this.a.getNumberOfRows()) {
								int col = 0;
								while (!found && col < this.a.getSeatsPerRow()) {
									if (col != j) {
										this.s[col].acquire();
									}
									if (this.a.isSeatEmpty(row, col)) {
										found = true;
										this.a.freeSeat(i, j);
										this.a.add(c, row, col);
									}
									if (col != j) {
										this.s[col].release();
									}
									col++;
								}
								row++;
							}
						}
					}
				}
				this.s[j].release();
			}
		}

		// updating counter
		if (found)
			this.nAgent2++;

	}

	// Agent3 : verifies whether two over60 sit together, and if yes,
	// one of the two is moved to the back seats
	public void agent3() throws InterruptedException {
		boolean found = false;

		// searching for pairs of over60 that sit together
		for (int i = 0; i < this.a.getNumberOfRows(); i++) {
			for (int j = 0; j < this.a.getSeatsPerRow() - 1; j++) {
				this.s[j].acquire();
				Customer c1 = this.a.getCustomer(i, j);
				Customer c2 = this.a.getCustomer(i, j + 1);
				if (c1 != null && c2 != null && c1.isOver60() && c2.isOver60()) {
					int i1 = 0;
					Customer c3 = null;
					for (int offset = 1; offset < this.a.getNumberOfRows(); offset++) {
						i1 = (i + offset) % this.a.getNumberOfRows();
						c3 = this.a.getCustomer(i1, j);
						if (c3 == null || !c3.isOver60()) {
							break;
						}
					}
					this.a.freeSeat(i1, j);
					this.a.add(c1, i1, j);
					this.a.freeSeat(i, j);
					if (c3 != null) {
						this.a.add(c3, i, j);
					}
				}
				this.s[j].release();
			}
		}
	}

	public void agent4() throws InterruptedException {
		for (int i = 0; i < this.a.getNumberOfRows(); i++) {
			for (int j = 0; j < this.a.getSeatsPerRow(); j++) {
				this.s[j].acquire();
				Customer c = this.a.getCustomer(i, j);
				this.a.freeSeat(i, j);
				if (c != null)
					this.a.add(c, i, j);
				this.s[j].release();
			}
		}
		this.nAgent4++;
	}

	// Resetting
	public void reset() {
		this.nAgent1 = 0;
		this.nAgent2 = 0;
		this.nAgent3 = 0;
		this.nAgent4 = 0;
		this.a.reset();
	}

	// Printing
	public String toString() {
		String print = "AirSimulation (agent1 moves " + this.nAgent1 + ", agent2 moves " + this.nAgent2 + ", "
				+ "agent3 moves " + this.nAgent3 + ", agent4 moves " + this.nAgent4 + ")\n";
		print = print + a.toString();
		return print;
	}

	// Simulation in sequential (main)
	public static void main(String[] args) throws InterruptedException {
		System.out.println("\n** Sequential execution **\n");
		if (args != null && args.length > 0 && args[0] != null && args[0].equals("animation")) {
			AirSimulation s = new AirSimulation();
			long startime = System.currentTimeMillis();
			while (!s.a.isFlightFull()) {
				s.agent1();
				s.agent2();
				s.agent3();
				s.agent4();
				System.out.println(s + s.a.cleanString());
				Thread.sleep(100);
			}
			long elapsed = System.currentTimeMillis() - startime;
			System.out.println(s + "\nTime = " + elapsed + "\n");
		} else {
			AirSimulation s = new AirSimulation();
			Thread a2 = new Thread(() -> {
				try {
					while (!s.a.isFlightFull()) {
						s.agent2();
						// TODO Auto-generated catch block
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			Thread a3 = new Thread(() -> {
				try {
					while (!s.a.isFlightFull()) {
						s.agent3();
						// TODO Auto-generated catch block
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			Thread a4 = new Thread(() -> {
				try {
					while (!s.a.isFlightFull()) {
						s.agent4();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			long startime = System.currentTimeMillis();
			a2.start();
			a3.start();
			a4.start();
			while (!s.a.isFlightFull()) {
				s.agent1();
				// s.agent4();
			}
			long elapsed = System.currentTimeMillis() - startime;
			System.out.println(s + "\nTime = " + elapsed + "\n");
		}
	}
}

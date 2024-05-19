import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

public class TestA {

	@Test
	public void test()  // attackers have first turn on game start
	{
		PlayableLogic gameLogic = new GameLogic();
		assertTrue(gameLogic.isSecondPlayerTurn());
	}
	
	@Test
	public void test2()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p1 = new Position(4,4);
		Position p2 = new Position(3,3);
		assertFalse(gameLogic.move(p1, p2)); // move in a diagonal
	}
	
	@Test
	public void test3()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p3 = new Position(4,4);
		Position p4 = new Position(3,3);
		assertFalse(gameLogic.move(p3, p4)); // move over another soldier
		
	}
	
	@Test
	public void test4()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p5 = new Position(4,4);
		Position p6 = new Position(3,3);
		assertFalse(gameLogic.move(p5, p6)); // pawn move into corner
	}
	
	@Test
	public void test5()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p7 = new Position(5,5);
		boolean isking = gameLogic.getPieceAtPosition(p7) instanceof King;
		assertTrue(isking); // king is in center
	}
	
	@Test
	public void test6()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p8 = new Position(5,2);
		assertNull(gameLogic.getPieceAtPosition(p8)); // empty position returns null
	}
	
	@Test
	public void test7()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p8 = new Position(5,2);
		Position p9 = new Position(5,1);
		gameLogic.move(p9, p8);
		assertNotEquals(gameLogic.getPieceAtPosition(p8),null); // empty position updates to non empty 
	}
	
	@Test
	public void defenderswin() {
		PlayableLogic gameLogic = new GameLogic();
		Position p1 = new Position(5,1);
		Position p2 = new Position(4,1);
		Position p3 = new Position(3,1);
		Position p4 = new Position(5,3);
		Position p5 = new Position(5,4);
		Position p6 = new Position(5,5);
		Position p7 = new Position(9,0);
		Position p8 = new Position(5,2);
		Position p9 = new Position(9,3);
		Position p10 = new Position(10,0);
		gameLogic.move(p1, p2);
		gameLogic.move(p4, p1);
		gameLogic.move(p2, p3);
		gameLogic.move(p5, p8);
		gameLogic.move(p3, p2);
		gameLogic.move(p6, p4);
		gameLogic.move(p2, p3);
		gameLogic.move(p4, p9);
		gameLogic.move(p3, p2);
		gameLogic.move(p9, p7);
		gameLogic.move(p2, p3);
		gameLogic.move(p7, p10);
		assertEquals(gameLogic.isGameFinished(), true);
	}
	
	@Test
	public void attackerswin()
	{
		PlayableLogic gameLogic = new GameLogic();
		Position p1 = new Position(7,10);
		Position p2 = new Position(8,10);
		Position p3 = new Position(7,5);
		Position p4 = new Position(7,10);
		Position p5 = new Position(7,0);
		Position p6 = new Position(8,0);
		Position p7 = new Position(6,5);
		Position p8 = new Position(10,3);
		Position p9 = new Position(7,3);
		Position p10 = new Position(7,9);
		Position p11 = new Position(8,10);
		Position p12 = new Position(8,6);
		Position p13 = new Position(5,5);
		Position p14 = new Position(8,5);
		Position p15 = new Position(8,0);
		Position p16 = new Position(8,4);
		Position p17 = new Position(5,7);
		Position p18 = new Position(4,7);
		Position p19 = new Position(7,3);
		Position p20 = new Position(7,5);
		gameLogic.move(p1, p2);
		gameLogic.move(p3, p4);
		gameLogic.move(p5, p6);
		gameLogic.move(p7, p3); 
		gameLogic.move(p8, p9); 
		gameLogic.move(p3, p10); 
		gameLogic.move(p11, p12); 
		gameLogic.move(p13, p14); 
		gameLogic.move(p15, p16); 
		gameLogic.move(p17, p18);
		gameLogic.move(p19, p20);
		assertEquals(gameLogic.isGameFinished(), true);
	}
	
	@Test
	public void testreset() {
		PlayableLogic gameLogic = new GameLogic();
		Position p1 = new Position(5,1);
		Position p2 = new Position(4,1);
		Position p3 = new Position(3,1);
		Position p4 = new Position(5,3);
		Position p5 = new Position(5,4);
		Position p6 = new Position(5,5);
		Position p7 = new Position(9,0);
		Position p8 = new Position(5,2);
		Position p9 = new Position(9,3);
		gameLogic.move(p1, p2);
		gameLogic.move(p4, p1);
		gameLogic.move(p2, p3);
		gameLogic.move(p5, p8);
		gameLogic.move(p3, p2);
		gameLogic.move(p6, p4);
		gameLogic.move(p2, p3);
		gameLogic.move(p4, p9);
		gameLogic.move(p3, p2);
		gameLogic.move(p9, p7);
		gameLogic.move(p2, p3);
		gameLogic.reset();
		
		// if one of those tests fails then -2 points but not more than that (treat it as one single test)
		assertNotEquals(gameLogic.getPieceAtPosition(p6),null);
		assertNotEquals(gameLogic.getPieceAtPosition(p1),null);
		assertNotEquals(gameLogic.getPieceAtPosition(p4),null);
		assertNotEquals(gameLogic.getPieceAtPosition(p5),null);
	}
}

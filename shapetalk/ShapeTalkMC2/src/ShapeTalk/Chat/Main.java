/*
 *M2C Multicast Chat
 *Coded in a boring train journey. Of course it lacks of weakness in several points... 
 *it's nothing but a time wasting game, so don't bother and enjoy it
 *Thanks to maffo for the first beta testing.
 *02/22/2007 Primiano Tucci
 */

package ShapeTalk.Chat;

public class Main {

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				Manager.GetInstance();
				frmStart.GetInstance().setVisible(true);
			}
		});

	}

}

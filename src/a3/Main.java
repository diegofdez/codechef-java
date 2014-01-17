package a3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Segment trees solution following explanation from
 * http://discuss.codechef.com/
 * questions/36507/a3-the-guessing-game-editorial-explanation
 * 
 * @author diegofdez
 * 
 */
public class Main
{

	public static boolean	debug	= false;
	public static Reader	in		= new Reader();
	public static Writer	out		= new Writer();

	public static final int	MAX_K	= 200000;
	public static final int	MAX_N	= 2 * MAX_K;

	public static int		t;

	public static int[]		count;
	public static int[]		touchedAt;
	public static final int	MIN		= Integer.MIN_VALUE;

	public static void main(String[] args)
	{
		count = new int[2 * MAX_N + 1];
		touchedAt = new int[2 * MAX_N + 1];

		Hint[] hints = new Hint[MAX_K];

		int T = in.readInt();

		for (t = 1; t <= T; t++) {
			final int k = in.readInt();

			for (int i = 0; i < k; i++) {
				hints[i] = new Hint(in.readLine());
			}

			Arrays.sort(hints, 0, k);

			int lastSeen = 1;
			int currentValue = 0;

			for (int i = 0; i < k; i++) {
				char operator = hints[i].operator;
				int value = hints[i].value;
				boolean yes = hints[i].yes;

				if (yes
						&& ((value == 1 && operator == '<') || (value == (int) 1e9 && operator == '>'))) {
					continue;
				}

				if (value > lastSeen) {
					if (value - lastSeen > 1) {
						currentValue++;
					}

					currentValue++;

					lastSeen = value;
				}

				value = currentValue;

				switch (operator) {
				case '<':
					if (yes) {
						add(0, value - 1, 0, MAX_N, 0);
					} else {
						add(value, MAX_N, 0, MAX_N, 0);
					}
					break;
				case '=':
					if (yes) {
						add(value, value, 0, MAX_N, 0);
					} else {
						add(0, value - 1, 0, MAX_N, 0);
						add(value + 1, MAX_N, 0, MAX_N, 0);
					}
					break;
				case '>':
					if (yes) {
						add(value + 1, MAX_N, 0, MAX_N, 0);
					} else {
						add(0, value, 0, MAX_N, 0);
					}
					break;
				}
			}

			out.println(k - maxRange(0, MAX_N, 0));
		}

		out.close();
	}

	public static void add(
			int rangeL,
			int rangeR,
			int treeL,
			int treeR,
			int treePos)
	{
		if (rangeL > treeR || rangeR < treeL) {
			return;
		}

		if (touchedAt[treePos] != t) {
			touchedAt[treePos] = t;
			count[treePos] = 0;
		}

		if (treeL >= rangeL && treeR <= rangeR) {
			count[treePos]++;
			return;
		}

		int mid = (treeL + treeR) / 2;

		add(rangeL, rangeR, treeL, mid, 2 * treePos + 1);
		add(rangeL, rangeR, mid + 1, treeR, 2 * treePos + 2);
	}

	public static int maxRange(int treeL, int treeR, int treePos)
	{
		if (touchedAt[treePos] == t) {
			int total = count[treePos];

			if (treeL == treeR) {
				return total;
			}

			int mid = (treeL + treeR) / 2;

			int max1 = maxRange(treeL, mid, 2 * treePos + 1);
			int max2 = maxRange(mid + 1, treeR, 2 * treePos + 2);

			return total + Math.max(max1, max2);
		}

		return 0;
	}
}

class Hint implements Comparable<Hint>
{
	final char		operator;
	final int		value;
	final boolean	yes;

	Hint(String s)
	{
		StringTokenizer content = new StringTokenizer(s);
		this.operator = content.nextToken().charAt(0);
		this.value = Integer.parseInt(content.nextToken());
		this.yes = content.nextToken().equals("Yes");
	}

	public int compareTo(Hint h)
	{
		return value - h.value;
	}

	public String toString()
	{
		return String.format("%c %d %s", operator, value, yes ? "Yes" : "No");
	}
}

class Reader
{
	private BufferedReader	input;
	private StringTokenizer	line	= new StringTokenizer("");

	public Reader()
	{
		input = new BufferedReader(new InputStreamReader(System.in));
	}

	public Reader(String s)
	{
		try {
			input = new BufferedReader(new FileReader(s));
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void fill()
	{
		try {
			if (!line.hasMoreTokens())
				line = new StringTokenizer(input.readLine());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public double nextDouble()
	{
		fill();
		return Double.parseDouble(line.nextToken());
	}

	public String nextWord()
	{
		fill();
		return line.nextToken();
	}

	public int nextInt()
	{
		fill();
		return Integer.parseInt(line.nextToken());
	}

	public long nextLong()
	{
		fill();
		return Long.parseLong(line.nextToken());
	}

	public double readDouble()
	{
		double d = 0;
		try {
			d = Double.parseDouble(input.readLine());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
		return d;
	}

	public int readInt()
	{
		int i = 0;
		try {
			i = Integer.parseInt(input.readLine());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
		return i;
	}

	public int[] readInts(int n)
	{
		int[] a = new int[n];
		for (int i = 0; i < n; i++)
			a[i] = nextInt();
		return a;
	}

	public void fillInts(int[] a)
	{
		for (int i = 0; i < a.length; i++)
			a[i] = nextInt();
	}

	public long readLong()
	{
		long l = 0;
		try {
			l = Long.parseLong(input.readLine());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
		return l;
	}

	public String readLine()
	{
		String s = "";
		try {
			s = input.readLine();
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
		return s;
	}
}

class Writer
{

	private BufferedWriter	output;

	public Writer()
	{
		output = new BufferedWriter(new OutputStreamWriter(System.out));
	}

	public Writer(String s)
	{
		try {
			output = new BufferedWriter(new FileWriter(s));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public void println()
	{
		try {
			output.append("\n");
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void print(Object o)
	{
		try {
			output.append(o.toString());
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void println(Object o)
	{
		try {
			output.append(o.toString() + "\n");
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void printf(String format, Object... args)
	{
		try {
			output.append(String.format(format, args));
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void printfln(String format, Object... args)
	{
		try {
			output.append(String.format(format, args) + "\n");
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void flush()
	{
		try {
			output.flush();
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}

	public void close()
	{
		try {
			output.close();
		} catch (IOException io) {
			io.printStackTrace();
			System.exit(0);
		}
	}
}

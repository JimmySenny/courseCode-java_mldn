package cn.mldn.demo;

public class MapBinaryTreeDemo {
	public static void main(String[] args) {
		MapBinaryTree<Integer,Person> tree = new MapBinaryTree<Integer,Person>() ;
		tree.add(80,new Person("小强-80",80));
		tree.add(30,new Person("小强-30",30));
		tree.add(50,new Person("小强-50",50));
		tree.add(60,new Person("小强-60",60));
		tree.add(90,new Person("小强-90",90));
		System.out.println(tree.get(90));
//		Object results [] = tree.toArray() ;
//		for (Object obj : results) {
//			MapBinaryTree.Entry<Integer,Person> entry = (MapBinaryTree.Entry<Integer,Person>) obj ;
//			System.out.println(entry.getKey() + "  ---  " + entry.getValue());
//		}
	}
}
/**
 * 实现二叉树操作 
 * @param <K> 进行数据存储的key，通过它进行查询，这个key是Comparable的子类
 * @param <V> 保存具体的数据信息
 */
class MapBinaryTree<K extends Comparable<K>,V> {
	public static class Entry<K extends Comparable<K>,V> implements Comparable<K> {
		private K key ;
		private V value ;
		public Entry(K key,V value) {
			this.key = key ;
			this.value = value ;
		}
		public K getKey() {
			return this.key ;
		}
		public V getValue() {
			return this.value ;
		}
		public int compareTo(K o) {
			return this.key.compareTo(o) ;
		};
		
	}
	private class Node { 
		private MapBinaryTree.Entry<K,V> data ; // 存放Comparable，可以比较大小
		private Node parent ;	// 保存父节点
		private Node left ; // 保存左子树
		private Node right ; // 保存右子树
		public Node(MapBinaryTree.Entry<K,V> data) {	// 构造方法直接负责进行数据的存储
			this.data = data ;
		}
		/**
		 * 实现节点数据的适当位置的存储
		 * @param newNode 创建的新节点
		 */
		public void addNode(Node newNode) {
			if (newNode.data.key.compareTo(this.data.key) <= 0) {	// 比当前节点数据小
				if (this.left == null) {	// 现在没有左子树
					this.left = newNode ; 	// 保存左子树
					newNode.parent = this ; // 保存父节点
				} else {	// 需要向左边继续判断
					this.left.addNode(newNode); // 继续向下判断
				}
			} else {	// 比根节点的数据要大
				if (this.right == null) {
					this.right = newNode ; // 没有右子树
					newNode.parent = this ; // 保存父节点
				} else {
					this.right.addNode(newNode); // 继续向下判断
				}
			}
		}
		/**
		 * 实现所有数据的获取处理，按照中序遍历的形式来完成
		 */
		public void toArrayNode() {
			if (this.left != null) {	// 有左子树
				this.left.toArrayNode();  // 递归调用
			}
			MapBinaryTree.this.returnData[MapBinaryTree.this.foot ++] = this.data ;
			if (this.right != null) {
				this.right.toArrayNode(); 
			}
		}
		/**
		 * 进行数据的检索处理
		 * @param data 要检索的数据
		 * @return 找到返回true，找不到返回false
		 */
		public boolean containsNode(MapBinaryTree.Entry<K,V> data) {
			if (data.key.compareTo(this.data.key) == 0) {
				return true ; // 查找到了
			} else if (data.key.compareTo(this.data.key) < 0) {	// 左边有数据
				if (this.left != null) {
					return this.left.containsNode(data) ;
				} else {
					return false ;
				}
			} else {
				if (this.right != null) {
					return this.right.containsNode(data) ;
				} else {
					return false ;
				}
			}
		} 
		public V getNode(K key) {
			if (key.compareTo(this.data.key) == 0) {
				return this.data.value ; // 获取数据
			} else if (key.compareTo(this.data.key) < 0) {
				if (this.left != null) {
					return this.left.getNode(key) ;
				} else {
					return null ;
				}
			} else {
				if (this.right != null) {
					return this.right.getNode(key) ;
				} else {
					return null ;
				}
			}
		}
	}
	// ------------ 以下为二叉树的功能实现 ----------------
	private Node root ; // 保存的是根节点
	private int count ; // 保存数据个数
	private Object [] returnData ; // 返回的数据
	private int foot = 0 ; // 脚标控制
	/**
	 * 进行数据的保存
	 * @param data 要保存的数据内容
	 * @exception NullPointerException 保存数据为空时抛出的异常
	 */
	public void add(K key,V value) {
		if (key == null || value == null) {
			throw new NullPointerException("保存的数据不允许为空！") ;
		}
		// 所有的数据本身不具备有节点关系的匹配，那么一定要将其包装在Node类之中
		Node newNode = new Node(new MapBinaryTree.Entry(key, value)) ;	// 保存节点
		if (this.root == null) {	// 现在没有根节点，则第一个节点作为根节点
			this.root = newNode ; 
		} else {	// 需要为其保存到一个合适的节点
			this.root.addNode(newNode) ; // 交由Node类负责处理
		}
		this.count ++ ;
	}
	/**
	 * 现在的检索主要依靠的是Comparable实现的数据比较
	 * @param data 要比较的数据
	 * @return 查找到数据返回true，否则返回false
	 */
	public boolean contains(K key) {
		if (this.count == 0) {	// 还没有数据
			return false ;
		}
		return this.root.containsNode(new MapBinaryTree.Entry(key, null)) ; // 该操作一定要交给Node类完成
	}
	/**
	 * 以对象数组的形式返回全部数据，如果没有数据返回null
	 * @return 全部数据
	 */
	public Object[] toArray() {
		if (this.count == 0) {
			return null ;
		}
		this.returnData = new Object[this.count] ; // 保存长度为数组长度
		this.foot = 0 ; // 脚标清零
		this.root.toArrayNode() ; // 直接通过Node类负责
		return this.returnData ; // 返回全部的数据
	}
	/**
	 * 根据指定的key获取对应的value数据
	 * @param key 要查询的key信息
	 * @return 如果有数据返回内容，如果没有返回null
	 */
	public V get(K key) {
		if (this.count == 0 || key == null) {
			return null ;
		}
		return this.root.getNode(key) ;
	}
}

class Person {
	private String name;
	private int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	// 无参构造、setter、getter略
	@Override
	public String toString() {
		return "【Person类对象】姓名：" + this.name + "、年龄：" + this.age + "\n";
	}
}